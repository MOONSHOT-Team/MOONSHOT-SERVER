package org.moonshot.server.domain.keyresult.dto.response;

import org.moonshot.server.domain.log.dto.response.LogResponseDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public record KRDetailResponseDto(
        String title,
        int progressBar,
        String krState,
        String startAt,
        String expireAt,
        List<LogResponseDto> logList
) {

    public static KRDetailResponseDto of(String title, int progressBar, String krState, LocalDateTime startAt, LocalDateTime expireAt, List<LogResponseDto> logList) {
        return new KRDetailResponseDto(title, progressBar, krState, startAt.format(DateTimeFormatter.ISO_LOCAL_DATE), expireAt.format(DateTimeFormatter.ISO_LOCAL_DATE), logList);
    }

}