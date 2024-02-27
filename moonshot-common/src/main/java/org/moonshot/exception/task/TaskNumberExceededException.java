package org.moonshot.exception.task;

import org.moonshot.exception.global.common.MoonshotException;
import org.moonshot.response.ErrorType;

public class TaskNumberExceededException extends MoonshotException {

    public TaskNumberExceededException() {
        super(ErrorType.ACTIVE_TASK_NUMBER_EXCEEDED);
    }

}
