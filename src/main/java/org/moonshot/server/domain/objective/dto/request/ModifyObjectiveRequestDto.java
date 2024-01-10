package org.moonshot.server.domain.objective.dto.request;

import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record ModifyObjectiveRequestDto(
        @NotNull(message = "대상의 ID를 입력해주세요.")
        long objectiveId,
        @NotNull(message = "목표 수정 종류를 입력해주세요.")
        boolean isClosed,
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime expireAt
) {
}
