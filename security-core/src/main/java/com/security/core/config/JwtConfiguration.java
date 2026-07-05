package com.security.core.config;

public record JwtConfiguration(
        String secret,
        String issuer,
        long accessTokenExpiration,
        long refreshTokenExpiration
) {
}