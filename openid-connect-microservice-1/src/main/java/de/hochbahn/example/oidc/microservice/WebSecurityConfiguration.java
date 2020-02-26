package de.hochbahn.example.oidc.microservice;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .anyRequest()
                .fullyAuthenticated()
                    .and()
                        .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                        .csrf().disable();
    }
}
