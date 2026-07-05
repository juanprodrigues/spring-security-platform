package com.msauth.controller;

import com.msauth.dto.request.LoginRequest;
import com.msauth.dto.request.RefreshRequest;
import com.msauth.dto.request.RegisterRequest;
import com.msauth.dto.response.LoginResponse;
import com.msauth.dto.response.RefreshResponse;
import com.msauth.dto.response.UsuarioResponse;
import com.msauth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(
            AuthService authService
    ) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioResponse> register(
            @Valid
            @RequestBody
            RegisterRequest request
    ) {

        return ResponseEntity.ok(
                authService.register(request)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid
            @RequestBody
            LoginRequest request
    ) {

        return ResponseEntity.ok(
                authService.login(request)
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refresh(
            @Valid
            @RequestBody
            RefreshRequest request
    ) {

        return ResponseEntity.ok(
                authService.refresh(request)
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestParam String refreshToken
    ) {

        authService.logout(refreshToken);

        return ResponseEntity.noContent()
                .build();
    }
}