package org.moonshot.server.domain.objective.dto.response;

import org.moonshot.server.domain.objective.model.Category;
import org.moonshot.server.domain.objective.model.Objective;

public record ObjectiveResponseDto(
        Long id,
        String title,
        String content,
        Category category,
        String date,
        short progress
) {
    public static ObjectiveResponseDto of(Objective objective) {
        return new ObjectiveResponseDto(
                objective.getId(),
                objective.getTitle(),
                objective.getContent(),
                objective.getCategory(),
                objective.getDateString(),
                objective.getProgress()
        );
    }
}
