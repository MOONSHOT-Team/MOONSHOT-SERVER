package org.moonshot.server.domain.task.dto.response;

import java.util.List;
import org.moonshot.server.domain.task.model.Task;

public record TaskResponseDto(
        Long id,
        String title
) {
    public static List<TaskResponseDto> of(List<Task> taskList) {
        return taskList.stream().map(task -> new TaskResponseDto(
                task.getId(),
                task.getTitle()
        )).toList();
    }
}
