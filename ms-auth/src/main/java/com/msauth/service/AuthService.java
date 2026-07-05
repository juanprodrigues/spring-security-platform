package com.msauth.service;

import com.msauth.dto.request.LoginRequest;
import com.msauth.dto.request.RefreshRequest;
import com.msauth.dto.request.RegisterRequest;
import com.msauth.dto.response.LoginResponse;
import com.msauth.dto.response.RefreshResponse;
import com.msauth.dto.response.UsuarioResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    UsuarioResponse register(RegisterRequest request);

    RefreshResponse refresh(RefreshRequest request);

    void logout(String refreshToken);
}