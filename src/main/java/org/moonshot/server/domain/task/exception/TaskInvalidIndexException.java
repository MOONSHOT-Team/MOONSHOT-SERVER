package org.moonshot.server.domain.task.exception;

import org.moonshot.server.global.common.exception.MoonshotException;
import org.moonshot.server.global.common.response.ErrorType;

public class TaskInvalidIndexException extends MoonshotException {

    public TaskInvalidIndexException() {
        super(ErrorType.INVALID_TASK_INDEX);
    }

}
