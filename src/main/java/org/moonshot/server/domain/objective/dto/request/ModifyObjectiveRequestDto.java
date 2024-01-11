package org.moonshot.server.domain.objective.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public record ModifyObjectiveRequestDto(
        @NotNull(message = "대상의 ID를 입력해주세요.")
        long objectiveId,
        @NotNull(message = "목표 수정 종류를 입력해주세요.")
        boolean isClosed,
        LocalDate expireAt
) {
}

