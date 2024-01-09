package org.moonshot.server.domain.log.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LogCreateRequestDto(
        Long keyResultId,

        @NotNull(message = "Log의 수치를 입력해주세요.")
        @Size(min = 1, max = 30, message = "진척정도의 수치 값은 30자 이하여야 합니다.")
        long logNum,

        @NotNull(message = "Log의 체크인 본문을 입력해주세요.")
        @Size(min = 1, max = 100, message = "본문은 100자 이하여야 합니다.")
        String logContent
) {
}
