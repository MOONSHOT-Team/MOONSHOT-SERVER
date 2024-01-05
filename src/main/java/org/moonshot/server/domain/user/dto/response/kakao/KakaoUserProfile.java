package org.moonshot.server.domain.user.dto.response.kakao;

public record KakaoUserProfile(
        String nickname,
        String profileImageUrl
) {
}