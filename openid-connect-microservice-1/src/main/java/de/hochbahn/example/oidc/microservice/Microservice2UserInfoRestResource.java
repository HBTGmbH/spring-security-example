package de.hochbahn.example.oidc.microservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "Microservice2UserInfoRestResource", url = "http://localhost:8081", path = "/user-info")
public interface Microservice2UserInfoRestResource {

    @GetMapping("/email")
    String getEmail(@RequestParam String id);

}
