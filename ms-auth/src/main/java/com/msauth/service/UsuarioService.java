package com.msauth.service;

import com.msauth.entity.Usuario;

import java.util.Optional;

public interface UsuarioService {

    Usuario save(Usuario usuario);

    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}