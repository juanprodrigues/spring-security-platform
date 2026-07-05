package com.msauth.mapper;

import com.msauth.dto.response.UsuarioResponse;
import com.msauth.entity.Usuario;

public final class UsuarioMapper {

    private UsuarioMapper() {
    }

    public static UsuarioResponse toResponse(
            Usuario usuario
    ) {

        return new UsuarioResponse(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getEnabled(),
                usuario.getCreatedAt()
        );
    }
}