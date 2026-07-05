package com.security.core.jwt;

public interface JwtValidator {

    boolean validate(String token);

    boolean isExpired(String token);
}