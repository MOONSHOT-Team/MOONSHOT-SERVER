package org.moonshot.keyresult.dto.response;

import java.util.List;
import org.moonshot.keyresult.model.KeyResult;
import org.moonshot.task.dto.response.TaskResponseDto;

public record KeyResultResponseDto(
        Long krId,
        String krTitle,
        Integer krIdx,
        List<TaskResponseDto> taskList
) {
    public static List<KeyResultResponseDto> of(List<KeyResult> krList) {
        return krList.stream().map(kr -> new KeyResultResponseDto(
                kr.getId(),
                kr.getTitle() + " : " + kr.getTarget() + kr.getMetric(),
                kr.getIdx(),
                TaskResponseDto.of(kr.getTaskList())
        )).toList();
    }
}
