package org.moonshot.exception.objective;

import org.moonshot.exception.global.common.MoonshotException;
import org.moonshot.response.ErrorType;

public class ObjectiveNumberExceededException extends MoonshotException {

    public ObjectiveNumberExceededException() {
        super(ErrorType.ACTIVE_OBJECTIVE_NUMBER_EXCEEDED);
    }

}
