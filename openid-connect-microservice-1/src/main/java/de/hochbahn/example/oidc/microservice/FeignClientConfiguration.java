package de.hochbahn.example.oidc.microservice;

import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;

@Configuration
public class FeignClientConfiguration {

    @Bean
    public RequestInterceptor oauth2BearerTokenInterceptor() {
        return requestTemplate -> {
            AbstractOAuth2TokenAuthenticationToken authentication = (AbstractOAuth2TokenAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            String accessToken = authentication.getToken().getTokenValue();
            requestTemplate.header("Authorization", "Bearer " + accessToken);
        };
    }

    @Bean
    public ErrorDecoder oauth2ErrorDecoder() {
        final ErrorDecoder defaultDecoder = new ErrorDecoder.Default();
        return (methodKey, response) -> {
            if(response.status() == 403) {
                throw new AccessDeniedException(response.reason());
            }
            return defaultDecoder.decode(methodKey, response);
        };
    }

}
