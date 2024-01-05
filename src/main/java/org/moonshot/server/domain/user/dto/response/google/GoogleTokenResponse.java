package org.moonshot.server.domain.user.dto.response.google;

public record GoogleTokenResponse(
        String accessToken,
        String refreshToken
) {
    public static GoogleTokenResponse of(String accessToken, String refreshToken) {
        return new GoogleTokenResponse(accessToken, refreshToken);
    }
}