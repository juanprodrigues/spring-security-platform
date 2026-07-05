package com.msauth.service;

import com.msauth.entity.RefreshToken;

public interface RefreshTokenService {

    RefreshToken save(RefreshToken refreshToken);

    RefreshToken findByToken(String token);

    void revoke(RefreshToken refreshToken);
}