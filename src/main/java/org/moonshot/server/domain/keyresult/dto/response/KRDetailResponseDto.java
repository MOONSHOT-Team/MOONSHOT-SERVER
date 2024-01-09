package org.moonshot.server.domain.keyresult.dto.response;

import org.moonshot.server.domain.keyresult.model.KRState;
import org.moonshot.server.domain.log.dto.response.LogResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public record KRDetailResponseDto(
        String title,
        int progressBar,
        KRState krState,
        LocalDateTime startAt,
        LocalDateTime expireAt,
        List<LogResponseDto> logList
) {

    public static KRDetailResponseDto of(String title, int progressBar, KRState krState, LocalDateTime startAt, LocalDateTime expireAt, List<LogResponseDto> logList) {
        return new KRDetailResponseDto(title, progressBar, krState, startAt, expireAt, logList);
    }

}
