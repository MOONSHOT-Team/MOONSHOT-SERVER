package org.moonshot.server.domain.keyresult.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

public record KeyResultModifyRequestDto(
        @NotNull(message = "수정할 KeyResult의 Objective Id를 입력해주세요.")
        Long objectiveId,
        @NotNull(message = "수정할 KeyResult Id를 입력해주세요")
        Long keyResultId,
        @Size(min = 1, max = 30, message = "KR은 30자 이하여야 합니다.")
        String title,
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime startAt,
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime expireAt,
        @Range(min = 0, max = 2, message = "KeyResult의 순서는 0부터 2까지로 설정할 수 있습니다.")
        Short idx,
        @Min(value = 1000, message = "목표 수치는 1000단위로 입력할 수 있습니다.")
        Integer target
) {
}
