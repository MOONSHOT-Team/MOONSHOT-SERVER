package org.moonshot.server.domain.log.dto.response;

import org.moonshot.server.domain.log.model.LogState;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record LogResponseDto(
        String logState,
        String dateTime,
        String title,
        String content
) {
    public static LogResponseDto of(String logState, LocalDateTime dateTime, String title, String content) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return new LogResponseDto(logState, dateTime.format(formatter), title, content);
    }
}
