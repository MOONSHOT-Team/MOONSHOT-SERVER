package org.moonshot.server.domain.objective.dto.response;

import java.util.List;
import org.moonshot.server.domain.objective.model.Objective;

public record ObjectiveGroupByYearDto(
        int year,
        List<HistoryObjectiveListDto> objList
) {
    public static ObjectiveGroupByYearDto of(Integer year, List<Objective> objList) {
        return new ObjectiveGroupByYearDto(
                year,
                objList.stream().map(HistoryObjectiveListDto::of).toList());
    }
}
