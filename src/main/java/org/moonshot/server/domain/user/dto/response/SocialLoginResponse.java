package org.moonshot.server.domain.user.dto.response;

import org.moonshot.server.global.auth.jwt.Token;

public record SocialLoginResponse(
        Long userId,
        String userName,
        Token token
) {
    public static SocialLoginResponse of(Long userId, String userName, Token token) {

        return new SocialLoginResponse(userId, userName, token);
    }
}
