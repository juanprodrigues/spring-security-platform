package com.security.core.jwt;

import com.security.core.model.JwtToken;
import com.security.core.model.JwtUser;

public interface JwtGenerator {

    JwtToken generateAccessToken(JwtUser user);

    JwtToken generateRefreshToken(JwtUser user);
}