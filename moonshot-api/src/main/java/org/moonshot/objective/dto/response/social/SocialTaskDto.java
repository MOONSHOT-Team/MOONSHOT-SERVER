package org.moonshot.objective.dto.response.social;

import org.moonshot.task.model.Task;

public record SocialTaskDto(
        String taskTitle,
        Integer taskIdx
) {
    public static SocialTaskDto of(Task task) {
        return new SocialTaskDto(
                task.getTitle(),
                task.getIdx()
        );
    }
}
