package org.moonshot.objective.dto.response;

import java.util.List;
import java.util.Map;

public record YearDto(
        Integer year,
        Integer count
) {
    public static List<YearDto> of(Map<Integer, Integer> yearMap) {
        return yearMap.entrySet().stream()
                .map(entry -> new YearDto(entry.getKey(), entry.getValue()))
                .sorted((a, b) -> b.year() - a.year())
                .toList();
    }
}
