package org.moonshot.server.domain.objective.dto.request;

import jakarta.validation.constraints.Size;

public record TaskCreateRequestDto(
        @Size(min = 1, max = 30, message = "Task는 30자 이하여야 합니다.")
        String title
) {
}
