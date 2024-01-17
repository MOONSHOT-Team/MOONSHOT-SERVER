package org.moonshot.server.domain.objective.dto.response;

import java.util.List;
import java.util.Map;
import org.moonshot.server.domain.objective.model.Criteria;

public record HistoryResponseDto(
        List<ObjectiveGroupByYearDto> groups,
        Map<Integer, Integer> years,
        List<String> categories
) {
    public static HistoryResponseDto of(List<ObjectiveGroupByYearDto> groups, Map<Integer, Integer> years,
                                        List<String> categories, Criteria criteria) {
        return new HistoryResponseDto(
                groups,
                years,
                categories.stream().distinct().toList()
        );
    }
}
