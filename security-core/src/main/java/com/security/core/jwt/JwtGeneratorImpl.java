package com.security.core.jwt;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import com.security.core.config.JwtConfiguration;
import com.security.core.model.JwtToken;
import com.security.core.model.JwtUser;
import com.security.core.util.JwtConstants;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JwtGeneratorImpl implements JwtGenerator {

    private final JwtConfiguration configuration;

    public JwtGeneratorImpl(JwtConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public JwtToken generateAccessToken(JwtUser user) {

        Instant now = Instant.now();

        Instant expiration =
                now.plusMillis(configuration.accessTokenExpiration());

        String token = buildToken(
                user,
                JwtConstants.ACCESS,
                now,
                expiration
        );

        return new JwtToken(token, now, expiration);
    }

    @Override
    public JwtToken generateRefreshToken(JwtUser user) {

        Instant now = Instant.now();

        Instant expiration =
                now.plusMillis(configuration.refreshTokenExpiration());

        String token = buildToken(
                user,
                JwtConstants.REFRESH,
                now,
                expiration
        );

        return new JwtToken(token, now, expiration);
    }

    private String buildToken(
            JwtUser user,
            String type,
            Instant issuedAt,
            Instant expiration
    ) {

        SecretKey key = Keys.hmacShaKeyFor(
                configuration.secret()
                        .getBytes(StandardCharsets.UTF_8)
        );

       return Jwts.builder()
        .subject(user.username())
        .issuer(configuration.issuer())
        .issuedAt(Date.from(issuedAt))
        .expiration(Date.from(expiration))
        .claim(JwtConstants.USER_ID, user.userId())
        .claim(JwtConstants.ROLES, user.roles())
        .claim(JwtConstants.TOKEN_TYPE, type)
        .signWith(key)
        .compact();
    }
}