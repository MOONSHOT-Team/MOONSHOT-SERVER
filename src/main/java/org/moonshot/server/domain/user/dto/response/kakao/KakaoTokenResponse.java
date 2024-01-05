package org.moonshot.server.domain.user.dto.response.kakao;

public record KakaoTokenResponse(
        String accessToken,
        String refreshToken
) {
    public static KakaoTokenResponse of(String accessToken, String refreshToken) {
        return new KakaoTokenResponse(accessToken, refreshToken);
    }
}
