package org.moonshot.server.domain.task.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;

public record TaskSingleCreateRequestDto(
        @NotNull(message = "KR이 생성될 KeyResult Id를 입력해주세요.")
        Long keyResultId,
        @Size(min = 1, max = 30, message = "Task는 30자 이하여야 합니다.")
        String title,
        @NotNull(message = "KR 순서를 입력해주세요")
        @Range(min = 0, max = 2, message = "KeyResult의 순서는 0부터 2까지로 설정할 수 있습니다.")
        Integer idx
) {
}
