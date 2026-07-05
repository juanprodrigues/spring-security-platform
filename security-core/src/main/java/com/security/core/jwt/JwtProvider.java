package com.security.core.jwt;

public interface JwtProvider extends
        TokenService,
        JwtGenerator,
        JwtValidator,
        JwtParser {
}