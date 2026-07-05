package com.msauth.dto.response;

import java.time.LocalDateTime;

public record UsuarioResponse(
        Long id,
        String username,
        String email,
        Boolean enabled,
        LocalDateTime createdAt
) {
}