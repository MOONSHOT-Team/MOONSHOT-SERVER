package org.moonshot.server.domain.objective.dto.response;

import java.time.LocalDateTime;

public record ModifyObjectiveResponseDto(
        Long objeciveId,
        boolean isClosed,
        LocalDateTime expireAt
) {
    public static ModifyObjectiveResponseDto of(Long objeciveId, boolean isClosed, LocalDateTime expireAt) {
        return new ModifyObjectiveResponseDto(objeciveId, isClosed, expireAt);
    }
}
