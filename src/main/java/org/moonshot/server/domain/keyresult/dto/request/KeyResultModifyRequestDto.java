package org.moonshot.server.domain.keyresult.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

import org.hibernate.validator.constraints.Range;
import org.moonshot.server.domain.keyresult.model.KRState;

public record KeyResultModifyRequestDto(
        @NotNull(message = "수정할 KeyResult Id를 입력해주세요")
        Long keyResultId,
        @Size(min = 1, max = 30, message = "KR은 30자 이하여야 합니다.")
        String title,
        LocalDate startAt,
        LocalDate expireAt,
        @Range(min = 1, max = 999999L, message = "수치는 999,999 이하여야 합니다.")
        Long target,
        KRState state,
        @Size(min = 1, max = 100, message = "본문은 100자 이하여야 합니다.")
        String logContent
) {
}
