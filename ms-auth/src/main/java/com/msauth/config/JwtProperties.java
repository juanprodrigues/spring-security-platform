package com.msauth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.jwt")
public record JwtProperties(

        String secret,

        String issuer,

        Long expiration,

        Long refreshExpiration
) {
}