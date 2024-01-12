package org.moonshot.server.domain.objective.dto.response;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.moonshot.server.domain.objective.model.Objective;

public record HistoryResponseDto(
        List<ObjectiveGroupByYearDto> groups,
        List<Integer> years,
        List<String> categories
) {
    public static HistoryResponseDto of(Map<Integer, List<Objective>> groups, List<String> categories) {
        return new HistoryResponseDto(
                groups.entrySet().stream().map(entry -> ObjectiveGroupByYearDto.of(entry.getKey(), entry.getValue())).toList(),
                groups.keySet().stream().distinct().toList(),
                categories.stream().distinct().toList()
        );
    }
}
