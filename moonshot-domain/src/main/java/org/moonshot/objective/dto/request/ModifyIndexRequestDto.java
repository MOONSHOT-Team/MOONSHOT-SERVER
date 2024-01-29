package org.moonshot.objective.dto.request;

import jakarta.validation.constraints.NotNull;
import org.moonshot.objective.model.IndexTarget;

public record ModifyIndexRequestDto(
        @NotNull(message = "대상의 ID를 입력해주세요.")
        Long id,
        @NotNull(message = "대상을 지정해주세요.")
        IndexTarget target,
        @NotNull(message = "대상의 이동할 위치 값을 입력하세요.")
        Integer idx
) {
}
