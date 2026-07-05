package com.security.core.jwt;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.crypto.SecretKey;

import com.security.core.config.JwtConfiguration;
import com.security.core.util.JwtConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JwtParserImpl implements JwtParser {

    private final JwtConfiguration configuration;

    public JwtParserImpl(JwtConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Claims extractClaims(String token) {

        SecretKey key = Keys.hmacShaKeyFor(
                configuration.secret().getBytes(StandardCharsets.UTF_8)
        );

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    @Override
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    @Override
    public Long extractUserId(String token) {
        return extractClaims(token)
                .get(JwtConstants.USER_ID, Long.class);
    }

    @Override
    public Set<String> extractRoles(String token) {

        List<String> roles =
                extractClaims(token)
                        .get(JwtConstants.ROLES, List.class);

        return new HashSet<>(roles);
    }
}