package com.msauth.dto.response;

import lombok.Builder;

@Builder
public record LoginResponse(
        String accessToken,
        String refreshToken,
        String tokenType
) {
}