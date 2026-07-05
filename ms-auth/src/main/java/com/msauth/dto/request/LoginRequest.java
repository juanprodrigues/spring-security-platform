package com.msauth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "Username es obligatorio")
    private String username;

    @NotBlank(message = "Password es obligatorio")
    private String password;
}