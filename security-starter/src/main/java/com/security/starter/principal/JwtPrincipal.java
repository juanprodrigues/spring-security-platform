package com.security.starter.principal;

import java.io.Serializable;
import java.util.Set;

public record JwtPrincipal(
        Long userId,
        String username,
        Set<String> roles
) implements Serializable {
}