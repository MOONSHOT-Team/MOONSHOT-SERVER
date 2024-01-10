package org.moonshot.server.domain.keyresult.dto.response;

import java.util.List;
import org.moonshot.server.domain.keyresult.model.KeyResult;
import org.moonshot.server.domain.task.dto.response.TaskResponseDto;

public record KeyResultResponseDto(
        Long keyResultId,
        String keyResultTitle,
        List<TaskResponseDto> taskList
) {
    public static List<KeyResultResponseDto> of(List<KeyResult> krList) {
        return krList.stream().map(kr -> new KeyResultResponseDto(
                kr.getId(),
                kr.getTitle(),
                TaskResponseDto.of(kr.getTaskList())
        )).toList();
    }
}