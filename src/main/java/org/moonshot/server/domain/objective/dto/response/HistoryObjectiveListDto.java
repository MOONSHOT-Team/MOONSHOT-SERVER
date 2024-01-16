package org.moonshot.server.domain.objective.dto.response;

import java.time.format.DateTimeFormatter;
import java.util.List;
import org.moonshot.server.domain.objective.model.Objective;

public record HistoryObjectiveListDto(
        Long objId,
        String title,
        String objCategory,
        int progress,
        String objPeriod,
        int objIdx,
        List<HistoryKeyResultDto> krList
) {
    public static HistoryObjectiveListDto of(Objective objective) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.MM.dd");
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
