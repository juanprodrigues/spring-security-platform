package com.security.core.model;

import java.time.Instant;

public record JwtToken(
        String token,
        Instant issuedAt,
        Instant expiresAt
) {
}