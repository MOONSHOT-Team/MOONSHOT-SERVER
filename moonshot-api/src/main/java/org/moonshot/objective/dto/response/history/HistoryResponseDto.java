package org.moonshot.objective.dto.response.history;

import java.util.List;

public record HistoryResponseDto(
        List<ObjectiveGroupByYearDto> groups,
        List<String> categories
) {
    public static HistoryResponseDto of(List<ObjectiveGroupByYearDto> groups, List<String> categories) {
        return new HistoryResponseDto(groups, categories.stream().distinct().toList());
    }
}
