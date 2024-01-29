package org.moonshot.objective.dto.response;

import java.util.List;
import java.util.Map;
import org.moonshot.objective.model.Criteria;

public record HistoryResponseDto(
        List<ObjectiveGroupByYearDto> groups,
        List<YearDto> years,
//        Map<Integer, Integer> years,
        List<String> categories
) {
    public static HistoryResponseDto of(List<ObjectiveGroupByYearDto> groups, Map<Integer, Integer> years,
                                        List<String> categories, Criteria criteria) {
        return new HistoryResponseDto(
                groups,
                YearDto.of(years),
                categories.stream().distinct().toList()
        );
    }
}
