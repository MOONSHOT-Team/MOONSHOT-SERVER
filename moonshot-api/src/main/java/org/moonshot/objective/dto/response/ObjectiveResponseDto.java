package org.moonshot.objective.dto.response;

import org.moonshot.objective.model.Objective;

public record ObjectiveResponseDto(
        Long id,
        String title,
        String content,
        String category,
        String date,
        short progress
) {
    public static ObjectiveResponseDto of(Objective objective) {
        return new ObjectiveResponseDto(
                objective.getId(),
                objective.getTitle(),
                objective.getContent(),
                objective.getCategory().getValue(),
                objective.getDateString(),
                objective.getProgress()
        );
    }
}
