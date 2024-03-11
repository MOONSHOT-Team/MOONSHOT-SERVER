package org.moonshot.task.service.validator;

import static org.moonshot.response.ErrorType.ACTIVE_TASK_NUMBER_EXCEEDED;
import static org.moonshot.response.ErrorType.INVALID_TASK_INDEX;

import org.moonshot.exception.BadRequestException;

public class TaskValidator {

    private static final int ACTIVE_TASK_NUMBER = 3;

    public static void validateTaskIndex(final int index, final int requestIndex) {
        if (index != requestIndex) {
            throw new BadRequestException(INVALID_TASK_INDEX);
        }
    }

    public static void validateActiveTaskSizeExceeded(final int taskListSize) {
        if (taskListSize >= ACTIVE_TASK_NUMBER) {
            throw new BadRequestException(ACTIVE_TASK_NUMBER_EXCEEDED);
        }
    }

    public static void validateIndexUnderMaximum(final int requestIndex, final int totalTaskListSize) {
        if (requestIndex > totalTaskListSize) {
            throw new BadRequestException(INVALID_TASK_INDEX);
        }
    }

    public static void validateIndex(final Long taskCount, final Integer requestIndex) {
        if (taskCount <= requestIndex || requestIndex < 0) {
            throw new BadRequestException(INVALID_TASK_INDEX);
        }
    }

}
