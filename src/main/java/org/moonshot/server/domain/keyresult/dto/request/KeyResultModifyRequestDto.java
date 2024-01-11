package org.moonshot.server.domain.keyresult.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import org.moonshot.server.domain.keyresult.model.KRState;
import org.moonshot.server.global.common.model.validator.ValidLimitValue;
import org.moonshot.server.global.common.model.validator.ValidTargetNumber;

public record KeyResultModifyRequestDto(
        @NotNull(message = "수정할 KeyResult Id를 입력해주세요")
        Long keyResultId,
        @Size(min = 1, max = 30, message = "KR은 30자 이하여야 합니다.")
        String title,
        LocalDate startAt,
        LocalDate expireAt,
        @ValidTargetNumber
        @ValidLimitValue
        Long target,
        KRState state,
        @Size(min = 1, max = 100, message = "본문은 100자 이하여야 합니다.")
        String logContent
) {
}
