package org.moonshot.task.dto.response;

import java.util.List;
import org.moonshot.task.model.Task;

public record TaskResponseDto(
        Long id,
        String title,
        Integer idx
) {
    public static List<TaskResponseDto> of(List<Task> taskList) {
        return taskList.stream().map(task -> new TaskResponseDto(
                task.getId(),
                task.getTitle(),
                task.getIdx()
        )).toList();
    }
}
