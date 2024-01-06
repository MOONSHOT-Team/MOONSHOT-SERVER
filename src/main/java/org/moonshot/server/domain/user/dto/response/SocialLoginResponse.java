package org.moonshot.server.domain.user.dto.response;

public record SocialLoginResponse(
        Long userId,
        String userName
) {
    public static SocialLoginResponse of(Long userId, String userName) {

        return new SocialLoginResponse(userId, userName);
    }
}
