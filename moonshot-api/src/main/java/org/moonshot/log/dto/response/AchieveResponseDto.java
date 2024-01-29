package org.moonshot.log.dto.response;

public record AchieveResponseDto(
        long objId,
        String nickname,
        short progress
) {
    public static AchieveResponseDto of(long objId, String nickname, short progress) {
        return new AchieveResponseDto(objId, nickname, progress);
    }
}