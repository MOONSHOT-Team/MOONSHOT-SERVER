package org.moonshot.objective.dto.response.social;

import org.moonshot.keyresult.model.KeyResult;

import java.time.format.DateTimeFormatter;
import java.util.List;

public record SocialKeyResultDto(
        String krTitle,
        String krStartAt,
        String krExpireAt,
        Long keyResultId,
        Integer krIdx,
        Long krTarget,
        String krMetric,
        List<SocialTaskDto> taskList
) {
    public static SocialKeyResultDto of(KeyResult keyResult) {
        return getSocialKeyResultDto(keyResult);
    }

    private static SocialKeyResultDto getSocialKeyResultDto(KeyResult keyResult) {
        return new SocialKeyResultDto(
                keyResult.getTitle(),
                keyResult.getPeriod().getStartAt().format(DateTimeFormatter.ISO_LOCAL_DATE),
                keyResult.getPeriod().getExpireAt().format(DateTimeFormatter.ISO_LOCAL_DATE),
                keyResult.getId(),
                keyResult.getIdx(),
                keyResult.getTarget(),
                keyResult.getMetric(),
                keyResult.getTaskList().stream().distinct().map(SocialTaskDto::of).toList()
        );
    }
}

