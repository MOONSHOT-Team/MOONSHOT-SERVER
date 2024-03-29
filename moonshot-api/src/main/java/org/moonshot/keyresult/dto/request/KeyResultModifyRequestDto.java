package org.moonshot.keyresult.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import org.hibernate.validator.constraints.Range;
import org.moonshot.keyresult.model.KRState;

public record KeyResultModifyRequestDto(
        @NotNull(message = "수정할 KeyResult Id를 입력해주세요")
        Long keyResultId,
        @Size(min = 1, max = 30, message = "KR은 30자 이하여야 합니다.")
        String krTitle,
        LocalDate krStartAt,
        LocalDate krExpireAt,
        @Range(min = 1, max = 999999L, message = "수치는 999,999 이하여야 합니다.")
        Long krTarget,
        KRState krState,
        @Size(min = 1, max = 100, message = "본문은 100자 이하여야 합니다.")
        String logContent
) {
}
