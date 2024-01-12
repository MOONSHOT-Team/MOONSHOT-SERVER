package org.moonshot.server.domain.objective.dto.response;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.moonshot.server.domain.objective.model.Objective;

public record ObjectiveGroupByYearDto(
        int year,
        int count,
        List<HistoryObjectiveListDto> objList
) {
    public static ObjectiveGroupByYearDto of(Integer year, List<Objective> objList) {
        return new ObjectiveGroupByYearDto(
                year,
                objList.size(),
                objList.stream().map(HistoryObjectiveListDto::of).toList());
    }
}
