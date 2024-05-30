package org.moonshot.objective.dto.response.history;

import java.util.List;

import org.moonshot.objective.model.Objective;

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
