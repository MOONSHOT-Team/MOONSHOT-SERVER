package org.moonshot.server.domain.objective.dto.request;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;
import org.moonshot.server.domain.objective.model.IndexTarget;

public record ModifyIndexRequestDto(
        @NotNull(message = "대상의 ID를 입력해주세요.")
        Long id,
        @NotNull(message = "대상을 지정해주세요.")
        IndexTarget target,
        @NotNull(message = "대상의 이동할 위치 값을 입력하세요.")
        @Range(min = 0, max = 2, message = "순서는 0부터 2까지로 설정할 수 있습니다.")
        Integer idx
) {
}
