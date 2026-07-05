package com.security.core.jwt;

import com.security.core.model.JwtToken;
import com.security.core.model.JwtUser;
import io.jsonwebtoken.Claims;

import java.util.Set;

public class JwtProviderImpl implements JwtProvider {

    private final JwtGenerator generator;
    private final JwtValidator validator;
    private final JwtParser parser;

    public JwtProviderImpl(
            JwtGenerator generator,
            JwtValidator validator,
            JwtParser parser
    ) {
        this.generator = generator;
        this.validator = validator;
        this.parser = parser;
    }

    @Override
    public JwtToken generateAccessToken(JwtUser user) {
        return generator.generateAccessToken(user);
    }

    @Override
    public JwtToken generateRefreshToken(JwtUser user) {
        return generator.generateRefreshToken(user);
    }

    @Override
    public boolean validate(String token) {
        return validator.validate(token);
    }

    @Override
    public boolean isExpired(String token) {
        return validator.isExpired(token);
    }

    @Override
    public Claims extractClaims(String token) {
        return parser.extractClaims(token);
    }

    @Override
    public String extractUsername(String token) {
        return parser.extractUsername(token);
    }

    @Override
    public Long extractUserId(String token) {
        return parser.extractUserId(token);
    }

    @Override
    public Set<String> extractRoles(String token) {
        return parser.extractRoles(token);
    }
}