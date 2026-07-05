package com.security.core.jwt;

import com.security.core.model.JwtToken;
import com.security.core.model.JwtUser;

import java.util.Set;

public interface TokenService {

    JwtToken generateAccessToken(JwtUser user);

    JwtToken generateRefreshToken(JwtUser user);

    boolean validate(String token);

    boolean isExpired(String token);

    String extractUsername(String token);

    Long extractUserId(String token);

    Set<String> extractRoles(String token);
}