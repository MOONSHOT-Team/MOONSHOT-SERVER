package org.moonshot.server.domain.keyresult.dto.response;

import java.time.LocalDate;
import org.moonshot.server.domain.log.dto.response.LogResponseDto;

import java.time.format.DateTimeFormatter;
import java.util.List;

public record KRDetailResponseDto(
        String title,
        long target,
        String metric,
        short progressBar,
        String krState,
        String startAt,
        String expireAt,
        List<LogResponseDto> logList
) {

    public static KRDetailResponseDto of(String title, long target, String metric, short progressBar, String krState, LocalDate startAt, LocalDate expireAt, List<LogResponseDto> logList) {
        return new KRDetailResponseDto(title + ": ", target, metric, progressBar, krState, startAt.format(DateTimeFormatter.ISO_LOCAL_DATE), expireAt.format(DateTimeFormatter.ISO_LOCAL_DATE), logList);
    }

}
