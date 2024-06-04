package org.moonshot.objective.dto.response.social;

import org.moonshot.objective.model.Objective;

import java.time.format.DateTimeFormatter;
import java.util.List;

public record SocialObjectiveDto(
        String objTitle,
        String objCategory,
        String objContent,
        String objStartAt,
        String objExpireAt,
        List<SocialKeyResultDto> krList
) {
    public static SocialObjectiveDto of(Objective objective) {
        return new SocialObjectiveDto(
                objective.getTitle(),
                objective.getCategory().getValue(),
                objective.getContent(),
                objective.getPeriod().getStartAt().format(DateTimeFormatter.ISO_LOCAL_DATE),
                objective.getPeriod().getExpireAt().format(DateTimeFormatter.ISO_LOCAL_DATE),
                objective.getKeyResultList().stream().distinct().map(SocialKeyResultDto::of).toList()
        );
    }
}
