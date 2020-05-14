package de.hochbahn.example.oidc;

import com.nimbusds.oauth2.sdk.*;
import com.nimbusds.oauth2.sdk.auth.ClientSecretBasic;
import com.nimbusds.oauth2.sdk.auth.Secret;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.id.Issuer;
import com.nimbusds.openid.connect.sdk.OIDCTokenResponseParser;
import com.nimbusds.openid.connect.sdk.op.OIDCProviderConfigurationRequest;
import com.nimbusds.openid.connect.sdk.op.OIDCProviderMetadata;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class AuthorizationCodeFlowAuthenticateWithCode {

  public static void main(String[] args) throws IOException, ParseException, URISyntaxException {
    String code = "b4d2fbb3-347c-415e-8d01-08d97877232a.83424159-e909-4e7a-b10e-69b37497d4e9.374956d4-13e7-4b2c-a664-6941f49fffeb"; // copy from redirect result of login form from browser

    // Parse OpenID provider metadata
    OIDCProviderMetadata opMetadata = OIDCProviderMetadata.parse(new OIDCProviderConfigurationRequest(new Issuer("http://localhost:8080/auth/realms/mosaic")).toHTTPRequest().send().getContentAsJSONObject());

    TokenResponse response = OIDCTokenResponseParser.parse(
            new TokenRequest(
                    opMetadata.getTokenEndpointURI(),
                    new ClientSecretBasic(new ClientID("kr_java"), new Secret("054ceb82-bc11-4286-a689-6dfc7258c338")),
                    new AuthorizationCodeGrant(new AuthorizationCode(code), new URI("http://localhost:8001/auth"))
            ).toHTTPRequest().send());
    if(response.indicatesSuccess()) {
      System.out.println(response.toSuccessResponse().getTokens().getRefreshToken());
      System.out.println(response.toSuccessResponse().getTokens().getAccessToken());
      System.out.println(response.toSuccessResponse().getTokens().getBearerAccessToken());
    } else {
      System.err.println(response.toErrorResponse().getErrorObject());
    }
  }

}
