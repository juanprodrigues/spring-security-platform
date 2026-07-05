package com.msauth.service.impl;

import com.msauth.entity.Usuario;
import com.msauth.repository.UsuarioRepository;
import com.msauth.service.UsuarioService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioServiceImpl
        implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(
            UsuarioRepository usuarioRepository
    ) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> findByUsername(
            String username
    ) {
        return usuarioRepository.findByUsername(username);
    }

    @Override
    public Optional<Usuario> findByEmail(
            String email
    ) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public boolean existsByUsername(
            String username
    ) {
        return usuarioRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(
            String email
    ) {
        return usuarioRepository.existsByEmail(email);
    }
}