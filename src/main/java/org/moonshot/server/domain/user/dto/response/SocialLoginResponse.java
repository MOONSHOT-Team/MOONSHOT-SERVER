package org.moonshot.server.domain.user.dto.response;

import org.moonshot.server.global.auth.jwt.TokenResponse;

public record SocialLoginResponse(
        Long userId,
        String userName,
        TokenResponse token
) {
    public static SocialLoginResponse of(Long userId, String userName, TokenResponse token) {

        return new SocialLoginResponse(userId, userName, token);
    }
}
