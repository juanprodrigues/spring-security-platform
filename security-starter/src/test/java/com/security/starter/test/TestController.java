package com.security.starter.test;

import com.security.starter.principal.JwtPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/private")
    public String privateEndpoint(
            @AuthenticationPrincipal JwtPrincipal principal
    ) {

        return principal.username();
    }

    @GetMapping("/auth/public")
    public String publicEndpoint() {
        return "public";
    }

    @GetMapping("/authentication")
    public String authentication(
            Authentication authentication
    ) {
        return authentication.getName();
    }
}