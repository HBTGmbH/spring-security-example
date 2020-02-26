package de.hochbahn.example.oidc.microservice;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.Authentication;

import java.io.Serializable;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler =
                new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(new MyPermissionEvaluator());
        return expressionHandler;
    }

    private static class MyPermissionEvaluator implements PermissionEvaluator {

        @Override
        public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
            return hasPermission(authentication, null, targetDomainObject.getClass().getSimpleName(), permission);
        }

        @Override
        public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
            if(targetType.equals("UserInfo") &&
                    permission.equals("read:email") &&
                    Integer.valueOf(targetId.toString()).intValue() < 1000 &&
                    authentication.getAuthorities().stream()
                            .map(a -> a.getAuthority())
                            .filter(a -> a.equals("ROLE_user"))
                            .findAny()
                            .isPresent()) {
                return true;
            }
            return false;
        }

    }

}
