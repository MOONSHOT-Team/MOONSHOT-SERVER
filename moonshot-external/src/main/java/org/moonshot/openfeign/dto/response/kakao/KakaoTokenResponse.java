package org.moonshot.openfeign.dto.response.kakao;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoTokenResponse(
        String accessToken,
        String refreshToken
) {
    public static KakaoTokenResponse of(String accessToken, String refreshToken) {
        return new KakaoTokenResponse(accessToken, refreshToken);
    }
}
