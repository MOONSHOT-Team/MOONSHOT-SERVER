package org.moonshot.exception.task;

import org.moonshot.exception.global.common.MoonshotException;
import org.moonshot.response.ErrorType;

public class TaskNotFoundException extends MoonshotException {

    public TaskNotFoundException() {
        super(ErrorType.NOT_FOUND_TASK_ERROR);
    }

}
