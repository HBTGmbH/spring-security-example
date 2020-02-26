package de.hochbahn.example.oidc.microservice;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-info")
public class UserInfoRestResource {

    @GetMapping("/email")
    @PreAuthorize("hasPermission(#id,'UserInfo','read:email')")
    //@PreAuthorize("hasRole('user')")
    public String getEmail(@RequestParam String id, @AuthenticationPrincipal JwtAuthenticationToken authority) {
        return String.valueOf(authority.getTokenAttributes().get("email"));
    }

}
