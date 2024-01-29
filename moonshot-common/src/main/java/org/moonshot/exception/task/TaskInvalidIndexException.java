package org.moonshot.exception.task;

import org.moonshot.exception.global.common.MoonshotException;
import org.moonshot.response.ErrorType;

public class TaskInvalidIndexException extends MoonshotException {

    public TaskInvalidIndexException() {
        super(ErrorType.INVALID_TASK_INDEX);
    }

}
