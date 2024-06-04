package org.moonshot.objective.dto.response.history;

import java.util.List;
import org.moonshot.keyresult.model.KeyResult;

public record HistoryKeyResultDto(
        Long krId,
        String krTitle,
        int krProgress,
        Integer krIdx,
        List<HistoryTaskDto> taskList
) {
    public static HistoryKeyResultDto of(KeyResult keyResult) {
        return new HistoryKeyResultDto(
                keyResult.getId(),
                keyResult.getTitle() + ": " + keyResult.getTarget() + keyResult.getMetric(),
                keyResult.getProgress(),
                keyResult.getIdx(),
                keyResult.getTaskList().stream().map(HistoryTaskDto::of).toList()
        );
    }
}

