package org.moonshot.objective.dto.response.history;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.moonshot.objective.model.Objective;

public record HistoryObjectiveListDto(
        Long objId,
        String objTitle,
        String objCategory,
        int objProgress,
        String objPeriod,
        int objIdx,
        List<HistoryKeyResultDto> krList
) {
    public static HistoryObjectiveListDto of(Objective objective) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy. MM. dd");
        String objPeriod = objective.getPeriod().getStartAt().format(formatter) + " - " + objective.getPeriod().getExpireAt()
                .format(formatter);
        return new HistoryObjectiveListDto(
                objective.getId(),
                objective.getTitle(),
                objective.getCategory().getValue(),
                objective.getProgress(),
                objPeriod,
                objective.getIdx(),
                objective.getKeyResultList().stream().map(HistoryKeyResultDto::of).toList()
        );
    }
}
