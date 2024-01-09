package org.moonshot.server.domain.log.dto.response;

import org.moonshot.server.domain.log.model.LogState;

import java.time.LocalDateTime;

public record LogResponseDto(
        LogState state,
        LocalDateTime date,
        String title,
        String content
) {
    public static LogResponseDto of(LogState state, LocalDateTime date, String title, String content) {
        return new LogResponseDto(state, date, title, content);
    }

}
