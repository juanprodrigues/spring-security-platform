package com.msauth.dto.response;

import lombok.Builder;

@Builder
public record RefreshResponse(
        String accessToken,
        String refreshToken
) {
}