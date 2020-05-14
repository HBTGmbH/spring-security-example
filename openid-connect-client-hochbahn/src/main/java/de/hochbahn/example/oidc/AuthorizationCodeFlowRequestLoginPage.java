package de.hochbahn.example.oidc;

import com.nimbusds.oauth2.sdk.*;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.id.Issuer;
import com.nimbusds.openid.connect.sdk.op.OIDCProviderConfigurationRequest;
import com.nimbusds.openid.connect.sdk.op.OIDCProviderMetadata;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class AuthorizationCodeFlowRequestLoginPage {

  public static void main(String[] args) throws ParseException, IOException, URISyntaxException {
    // Parse OpenID provider metadata
    OIDCProviderMetadata opMetadata = OIDCProviderMetadata.parse(new OIDCProviderConfigurationRequest(new Issuer("http://localhost:8080/auth/realms/mosaic")).toHTTPRequest().send().getContentAsJSONObject());

    AuthorizationRequest authenticationRequest = new AuthorizationRequest.Builder(ResponseType.getDefault(), new ClientID("kr_java"))
      .scope(Scope.parse("phone"))
      .endpointURI(opMetadata.getAuthorizationEndpointURI())
      .redirectionURI(new URI("http://localhost:8001/auth"))
      .build();

    HTTPRequest request = authenticationRequest.toHTTPRequest();
    StringBuilder sb = new StringBuilder();
    sb.append(request.getURL())
      .append('?')
      .append(request.getQuery());
    if(request.getFragment() != null) {
      sb.append('#').append(request.getFragment());
    }

    System.out.println(sb);
  }

}
