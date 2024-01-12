package org.moonshot.server.domain.objective.dto.response;

import org.moonshot.server.domain.task.model.Task;

public record HistoryTaskDto(
        Long taskId,
        String taskTitle,
        Integer taskIdx
) {
    public static HistoryTaskDto of(Task task) {
        return new HistoryTaskDto(
                task.getId(),
                task.getTitle(),
                task.getIdx()
        );
    }
}
