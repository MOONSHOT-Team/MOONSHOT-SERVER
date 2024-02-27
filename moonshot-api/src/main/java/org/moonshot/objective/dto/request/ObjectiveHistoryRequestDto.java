package org.moonshot.objective.dto.request;


import org.moonshot.objective.model.Category;

public record ObjectiveHistoryRequestDto(
        Integer year,
        Category category,
        String criteria
) {
}
