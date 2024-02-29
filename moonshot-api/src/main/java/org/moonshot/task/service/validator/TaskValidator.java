package org.moonshot.task.service.validator;

import org.moonshot.exception.keyresult.KeyResultInvalidIndexException;
import org.moonshot.exception.task.TaskInvalidIndexException;
import org.moonshot.exception.task.TaskNumberExceededException;

public class TaskValidator {

    private static final int ACTIVE_TASK_NUMBER = 3;

    public static void validateTaskIndex(final int index, final int requestIndex) {
        if (index != requestIndex) {
            throw new TaskInvalidIndexException();
        }
    }

    public static void validateActiveTaskSizeExceeded(final int taskListSize) {
        if (taskListSize >= ACTIVE_TASK_NUMBER) {
            throw new TaskNumberExceededException();
        }
    }

    public static void validateIndexUnderMaximum(final int requestIndex, final int totalTaskListSize) {
        if (requestIndex > totalTaskListSize) {
            throw new TaskInvalidIndexException();
        }
    }

}
