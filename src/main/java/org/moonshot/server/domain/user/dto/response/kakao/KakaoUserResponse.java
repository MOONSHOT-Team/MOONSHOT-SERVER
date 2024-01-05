package org.moonshot.server.domain.user.dto.response.kakao;

public record KakaoUserResponse(
        String id,
        KakaoAccount kakaoAccount
) {
}