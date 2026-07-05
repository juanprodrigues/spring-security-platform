package com.security.core.jwt;

import java.util.Set;

import io.jsonwebtoken.Claims;

public interface JwtParser {

    Claims extractClaims(String token);

    String extractUsername(String token);

    Long extractUserId(String token);

    Set<String> extractRoles(String token);
}