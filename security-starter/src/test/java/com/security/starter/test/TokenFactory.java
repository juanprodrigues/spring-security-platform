package com.security.starter.test;

import com.security.core.config.JwtConfiguration;
import com.security.core.jwt.JwtProvider;
import com.security.core.jwt.JwtProviderFactory;
import com.security.core.model.JwtUser;

import java.util.Set;

public final class TokenFactory {

    private TokenFactory() {
    }

    public static String generateToken() {

        JwtConfiguration configuration =
                new JwtConfiguration(
                        "12345678901234567890123456789012",
                        "empresa",
                        3600000,
                        604800000
                );

        JwtProvider provider =
                JwtProviderFactory.create(configuration);

        return provider.generateAccessToken(
                        new JwtUser(
                                1L,
                                "admin",
                                Set.of("ADMIN")
                        ))
                .token();
    }
}