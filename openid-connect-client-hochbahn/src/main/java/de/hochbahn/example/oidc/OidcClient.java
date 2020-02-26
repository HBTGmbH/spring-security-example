package de.hochbahn.example.oidc;

import com.nimbusds.oauth2.sdk.*;
import com.nimbusds.oauth2.sdk.auth.ClientAuthentication;
import com.nimbusds.oauth2.sdk.auth.ClientSecretPost;
import com.nimbusds.oauth2.sdk.auth.Secret;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.id.Issuer;
import com.nimbusds.oauth2.sdk.token.Tokens;
import com.nimbusds.openid.connect.sdk.*;
import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import com.nimbusds.openid.connect.sdk.op.OIDCProviderConfigurationRequest;
import com.nimbusds.openid.connect.sdk.op.OIDCProviderMetadata;

import java.io.IOException;
import java.net.URI;

public class OidcClient {

    public static void main(String[] args) throws IOException, ParseException {
        Issuer issuer = new Issuer("http://localhost:8180/auth/realms/mobi-platform");

        // Will resolve the OpenID provider metadata automatically
        OIDCProviderConfigurationRequest request = new OIDCProviderConfigurationRequest(issuer);

        // Make HTTP request
        HTTPRequest httpRequest = request.toHTTPRequest();
        HTTPResponse httpResponse = httpRequest.send();

        // Parse OpenID provider metadata
        OIDCProviderMetadata opMetadata = OIDCProviderMetadata.parse(httpResponse.getContentAsJSONObject());

        // Authenticate
        String username = "klaus.richarz";
        Secret password = new Secret("hamburg1!");
        AuthorizationGrant passwordGrant = new ResourceOwnerPasswordCredentialsGrant(username, password);

        // The credentials to authenticate the client at the token endpoint
        ClientID clientID = new ClientID("postman_test");
        Secret clientSecret = new Secret("3e997379-6dca-44ec-87a5-30006ab93188");
        ClientAuthentication clientAuth = new ClientSecretPost(clientID, clientSecret);

        // The request scope for the token
        Scope scope = new Scope("openid", "email");

        // The token endpoint
        URI tokenEndpoint = opMetadata.getTokenEndpointURI();

        // Make the token request
        TokenRequest tokenRequest = new TokenRequest(tokenEndpoint, clientAuth, passwordGrant, scope);

        TokenResponse tokenResponse = OIDCTokenResponseParser.parse(tokenRequest.toHTTPRequest().send());
        if(tokenResponse.indicatesSuccess()) {
            Tokens tokens = tokenResponse.toSuccessResponse().getTokens();
            System.out.println(tokens.toOIDCTokens().getIDToken().getParsedString());
        } else {
            System.out.println(tokenResponse.toErrorResponse().getErrorObject());
            return;
        }

        // Get User Info
        // Make the request
        httpResponse = new UserInfoRequest(opMetadata.getUserInfoEndpointURI(), tokenResponse.toSuccessResponse().getTokens().getBearerAccessToken())
                .toHTTPRequest()
                .send();

        // Parse the response
        UserInfoResponse userInfoResponse = UserInfoResponse.parse(httpResponse);

        if (! userInfoResponse.indicatesSuccess()) {
            // The request failed, e.g. due to invalid or expired token
            System.out.println(userInfoResponse.toErrorResponse().getErrorObject().getCode());
            System.out.println(userInfoResponse.toErrorResponse().getErrorObject().getDescription());
            return;
        }

        // Extract the claims
        UserInfo userInfo = userInfoResponse.toSuccessResponse().getUserInfo();
        System.out.println("Subject: " + userInfo.getSubject());
        System.out.println("Email: " + userInfo.getEmailAddress());
        System.out.println("Name: " + userInfo.getName());

        // Logout
        LogoutRequest logoutRequest = new LogoutRequest(opMetadata.getEndSessionEndpointURI(), tokenResponse.toSuccessResponse().getTokens().toOIDCTokens().getIDToken());
        httpResponse = logoutRequest.toHTTPRequest().send();
        System.out.println(httpResponse.getStatusCode());

    }

}
