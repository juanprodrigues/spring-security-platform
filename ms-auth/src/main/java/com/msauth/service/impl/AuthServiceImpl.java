package com.msauth.service.impl;

import com.msauth.dto.request.LoginRequest;
import com.msauth.dto.request.RefreshRequest;
import com.msauth.dto.request.RegisterRequest;
import com.msauth.dto.response.LoginResponse;
import com.msauth.dto.response.RefreshResponse;
import com.msauth.dto.response.UsuarioResponse;
import com.msauth.entity.RefreshToken;
import com.msauth.entity.Rol;
import com.msauth.entity.Usuario;
import com.msauth.exception.InvalidCredentialsException;
import com.msauth.exception.UserAlreadyExistsException;
import com.msauth.mapper.UsuarioMapper;
import com.msauth.service.AuthService;
import com.msauth.service.RefreshTokenService;
import com.msauth.service.RolService;
import com.msauth.service.UsuarioService;
import com.security.core.jwt.JwtProvider;
import com.security.core.model.JwtToken;
import com.security.core.model.JwtUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private final UsuarioService usuarioService;
    private final RolService rolService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AuthServiceImpl(
            UsuarioService usuarioService,
            RolService rolService,
            RefreshTokenService refreshTokenService,
            PasswordEncoder passwordEncoder,
            JwtProvider jwtProvider
    ) {
        this.usuarioService = usuarioService;
        this.rolService = rolService;
        this.refreshTokenService = refreshTokenService;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public UsuarioResponse register(RegisterRequest request) {

        if (usuarioService.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException(
                    "Username ya registrado"
            );
        }

        if (usuarioService.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException(
                    "Email ya registrado"
            );
        }

        Rol roleUser =
                rolService.findByNombre("ROLE_USER");

        Usuario usuario = Usuario.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(
                        passwordEncoder.encode(
                                request.getPassword()
                        )
                )
                .enabled(true)
                .build();

        usuario.getRoles().add(roleUser);

        usuario = usuarioService.save(usuario);

        return UsuarioMapper.toResponse(usuario);
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        Usuario usuario =
                usuarioService.findByUsername(
                        request.getUsername()
                ).orElseThrow(() ->
                        new InvalidCredentialsException(
                                "Credenciales inválidas"
                        )
                );

        if (!passwordEncoder.matches(
                request.getPassword(),
                usuario.getPassword()
        )) {

            throw new InvalidCredentialsException(
                    "Credenciales inválidas"
            );
        }

        Set<String> roles =
                usuario.getRoles()
                        .stream()
                        .map(Rol::getNombre)
                        .collect(Collectors.toSet());

        JwtUser jwtUser =
                new JwtUser(
                        usuario.getId(),
                        usuario.getUsername(),
                        roles
                );

        JwtToken accessToken =
                jwtProvider.generateAccessToken(jwtUser);

        JwtToken refreshJwt =
                jwtProvider.generateRefreshToken(jwtUser);

        RefreshToken refreshToken =
                RefreshToken.builder()
                        .token(refreshJwt.token())
                        .expiration(
                                LocalDateTime.ofInstant(
                                        refreshJwt.expiresAt(),
                                        ZoneOffset.UTC
                                )
                        )
                        .usuario(usuario)
                        .revoked(false)
                        .build();

        refreshTokenService.save(refreshToken);

        return LoginResponse.builder()
                .accessToken(accessToken.token())
                .refreshToken(refreshJwt.token())
                .tokenType("Bearer")
                .build();
    }

    @Override
    public RefreshResponse refresh(
            RefreshRequest request
    ) {

        RefreshToken refreshToken =
                refreshTokenService.findByToken(
                        request.getRefreshToken()
                );

        if (refreshToken.getRevoked()) {
            throw new InvalidCredentialsException(
                    "Refresh token revocado"
            );
        }

        if (!jwtProvider.validate(
                refreshToken.getToken()
        )) {

            throw new InvalidCredentialsException(
                    "Refresh token inválido"
            );
        }

        Usuario usuario =
                refreshToken.getUsuario();

        Set<String> roles =
                usuario.getRoles()
                        .stream()
                        .map(Rol::getNombre)
                        .collect(Collectors.toSet());

        JwtUser jwtUser =
                new JwtUser(
                        usuario.getId(),
                        usuario.getUsername(),
                        roles
                );

        JwtToken newAccessToken =
                jwtProvider.generateAccessToken(jwtUser);

        JwtToken newRefreshToken =
                jwtProvider.generateRefreshToken(jwtUser);

        refreshToken.setToken(
                newRefreshToken.token()
        );

        refreshToken.setExpiration(
                LocalDateTime.ofInstant(
                        newRefreshToken.expiresAt(),
                        ZoneOffset.UTC
                )
        );

        refreshTokenService.save(refreshToken);

        return RefreshResponse.builder()
                .accessToken(
                        newAccessToken.token()
                )
                .refreshToken(
                        newRefreshToken.token()
                )
                .build();
    }

    @Override
    public void logout(
            String refreshToken
    ) {

        RefreshToken token =
                refreshTokenService.findByToken(
                        refreshToken
                );

        refreshTokenService.revoke(token);
    }
}