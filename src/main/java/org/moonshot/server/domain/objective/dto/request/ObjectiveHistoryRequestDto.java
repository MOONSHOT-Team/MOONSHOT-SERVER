package org.moonshot.server.domain.objective.dto.request;

import org.moonshot.server.domain.objective.model.Category;

public record ObjectiveHistoryRequestDto(
        Integer year,
        Category category,
        String criteria
) {
}
