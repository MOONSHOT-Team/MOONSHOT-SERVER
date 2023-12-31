package org.moonshot.server.domain.keyresult.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record KeyResultDeleteRequestDto(
        @NotNull
        List<Long> keyResultIds
) {
}
