package org.moonshot.server.domain.log.dto.response;

public record AchieveResponseDto(
        String nickname,
        short progress
) {
    public static AchieveResponseDto of(String nickname, short progress) {
        return new AchieveResponseDto(nickname, progress);
    }
}