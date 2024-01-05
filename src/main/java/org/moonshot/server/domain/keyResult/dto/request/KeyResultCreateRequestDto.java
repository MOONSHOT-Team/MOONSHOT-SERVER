package org.moonshot.server.domain.keyresult.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record KeyResultCreateRequestDto(
        @NotNull(message = "KR이 생성될 ObjectiveId를 입력해주세요.")
        Long objectiveId,
        @NotNull @Valid @Size(max = 3)
        List<KeyResultCreateRequestInfoDto> krList
) {
}
