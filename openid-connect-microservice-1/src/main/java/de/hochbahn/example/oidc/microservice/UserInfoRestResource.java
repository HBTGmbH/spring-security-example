package de.hochbahn.example.oidc.microservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-info")
public class UserInfoRestResource {

    @Autowired
    Microservice2UserInfoRestResource microservice2;

    @GetMapping("/email")
    public String getEmail(@RequestParam String id, @AuthenticationPrincipal JwtAuthenticationToken authority) {
        return microservice2.getEmail(id);
    }

}
