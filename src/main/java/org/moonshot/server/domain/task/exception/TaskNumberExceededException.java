package org.moonshot.server.domain.task.exception;

import org.moonshot.server.global.common.exception.MoonshotException;
import org.moonshot.server.global.common.response.ErrorType;

public class TaskNumberExceededException extends MoonshotException {
    public TaskNumberExceededException() {
        super(ErrorType.ACTIVE_TASK_NUMBER_EXCEEDED);
    }
}
