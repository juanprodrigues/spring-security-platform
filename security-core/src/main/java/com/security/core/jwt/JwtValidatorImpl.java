package com.security.core.jwt;

import com.security.core.exception.InvalidTokenException;
import com.security.core.exception.JwtException;

public class JwtValidatorImpl implements JwtValidator {

    private final JwtParser parser;

    public JwtValidatorImpl(JwtParser parser) {
        this.parser = parser;
    }

    @Override
    public boolean validate(String token) {

        try {
            parser.extractClaims(token);
            return true;
        } catch (JwtException ex) {
            throw new InvalidTokenException("Invalid JWT token");
        }
    }

    @Override
    public boolean isExpired(String token) {

        return parser.extractClaims(token)
                .getExpiration()
                .before(new java.util.Date());
    }
}