package de.hochbahn.example.oidc;

import com.nimbusds.oauth2.sdk.*;
import com.nimbusds.oauth2.sdk.auth.ClientSecretBasic;
import com.nimbusds.oauth2.sdk.auth.Secret;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.id.Issuer;
import com.nimbusds.oauth2.sdk.token.RefreshToken;
import com.nimbusds.openid.connect.sdk.OIDCTokenResponseParser;
import com.nimbusds.openid.connect.sdk.op.OIDCProviderConfigurationRequest;
import com.nimbusds.openid.connect.sdk.op.OIDCProviderMetadata;

import java.io.IOException;

public class AuthorizationCodeFlowTokenRefresh {

  public static void main(String[] args) throws IOException, ParseException {
    String token = "eyJhbGciOiJIUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJhZTY0ZmEyMS0wNjcxLTRkZTUtYTczNC0wODRhM2VlYzViYzIifQ.eyJleHAiOjE1ODkzMDQwMzIsImlhdCI6MTU4OTMwMjIzMiwianRpIjoiZDVlMDMzY2UtY2ExNi00MDk0LWExZjItM2IxNTBkMjY3ZTJmIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL2F1dGgvcmVhbG1zL21vc2FpYyIsImF1ZCI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MC9hdXRoL3JlYWxtcy9tb3NhaWMiLCJzdWIiOiIyZTM4NDU0NC02ZmQ2LTQ0MTctOWU5OS1hYjJhZWE4ZTcxNTciLCJ0eXAiOiJSZWZyZXNoIiwiYXpwIjoia3JfamF2YSIsInNlc3Npb25fc3RhdGUiOiI4MzQyNDE1OS1lOTA5LTRlN2EtYjEwZS02OWIzNzQ5N2Q0ZTkiLCJzY29wZSI6InByb2ZpbGUgZW1haWwgcGhvbmUifQ.rbV8FS6JJjFe-vgHUxnHVOwdX48CFNfB7f6RYQ3wmpc";
    // Parse OpenID provider metadata
    OIDCProviderMetadata opMetadata = OIDCProviderMetadata.parse(new OIDCProviderConfigurationRequest(new Issuer("http://localhost:8080/auth/realms/mosaic")).toHTTPRequest().send().getContentAsJSONObject());

    TokenResponse response = OIDCTokenResponseParser.parse(
            new TokenRequest(
                    opMetadata.getTokenEndpointURI(),
                    new ClientSecretBasic(new ClientID("kr_java"), new Secret("054ceb82-bc11-4286-a689-6dfc7258c338")),
                    new RefreshTokenGrant(new RefreshToken(token))
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
