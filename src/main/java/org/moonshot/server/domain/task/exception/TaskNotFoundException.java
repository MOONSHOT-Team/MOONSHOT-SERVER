package org.moonshot.server.domain.task.exception;

import org.moonshot.server.global.common.exception.MoonshotException;
import org.moonshot.server.global.common.response.ErrorType;

public class TaskNotFoundException extends MoonshotException {

    public TaskNotFoundException() {
        super(ErrorType.NOT_FOUND_TASK_ERROR);
    }

}
