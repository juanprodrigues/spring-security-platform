package com.msauth.service.impl;

import com.msauth.entity.RefreshToken;
import com.msauth.exception.RefreshTokenNotFoundException;
import com.msauth.repository.RefreshTokenRepository;
import com.msauth.service.RefreshTokenService;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenServiceImpl
        implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenServiceImpl(
            RefreshTokenRepository refreshTokenRepository
    ) {
        this.refreshTokenRepository =
                refreshTokenRepository;
    }

    @Override
    public RefreshToken save(
            RefreshToken refreshToken
    ) {
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken findByToken(
            String token
    ) {

        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() ->
                        new RefreshTokenNotFoundException(
                                "Refresh token no encontrado"
                        )
                );
    }

    @Override
    public void revoke(
            RefreshToken refreshToken
    ) {

        refreshToken.setRevoked(true);

        refreshTokenRepository.save(refreshToken);
    }
}