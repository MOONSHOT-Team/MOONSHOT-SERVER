package org.moonshot.server.domain.objective.dto.response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record ModifyObjectiveResponseDto(
        Long objectiveId,
        boolean isClosed,
        String expireAt
) {
    public static ModifyObjectiveResponseDto of(Long objectiveId, boolean isClosed, LocalDateTime expireAt) {
        return new ModifyObjectiveResponseDto(objectiveId, isClosed, expireAt.format(DateTimeFormatter.ISO_DATE));
    }
}
