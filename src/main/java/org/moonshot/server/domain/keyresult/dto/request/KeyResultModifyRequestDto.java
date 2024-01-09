package org.moonshot.server.domain.keyresult.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import org.moonshot.server.domain.keyresult.model.KRState;
import org.moonshot.server.global.common.model.validator.ValidTargetNumber;
import org.springframework.format.annotation.DateTimeFormat;

public record KeyResultModifyRequestDto(
        @NotNull(message = "수정할 KeyResult Id를 입력해주세요")
        Long keyResultId,
        @Size(min = 1, max = 30, message = "KR은 30자 이하여야 합니다.")
        String title,
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime startAt,
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime expireAt,
        @ValidTargetNumber
        Long target,
        KRState state,
        @Size(min = 1, max = 100, message = "본문은 100자 이하여야 합니다.")
        String logContent
) {
}
