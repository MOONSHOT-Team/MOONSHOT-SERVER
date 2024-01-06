package org.moonshot.server.domain.keyresult.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import org.hibernate.validator.constraints.Range;
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
        Integer target
) {
}
