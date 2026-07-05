package com.security.core.model;

import java.util.Set;

public record JwtUser(
        Long userId,
        String username,
        Set<String> roles
) {
}