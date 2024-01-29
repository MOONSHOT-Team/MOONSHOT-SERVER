package org.moonshot.user.dto.response;


import org.moonshot.jwt.TokenResponse;

public record SocialLoginResponse(
        Long userId,
        String userName,
        TokenResponse token
) {
    public static SocialLoginResponse of(Long userId, String userName, TokenResponse token) {
        return new SocialLoginResponse(userId, userName, TokenResponse.of(token.accessToken(), token.refreshToken()));
    }
}
