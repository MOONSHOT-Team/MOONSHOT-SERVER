package org.moonshot.log.dto.response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record LogResponseDto(
        String logState,
        String dateTime,
        String title,
        String content
) {
    public static LogResponseDto of(String logState, LocalDateTime localDateTime, String title, String content) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return new LogResponseDto(logState, localDateTime.format(formatter), title, content);
    }
}
