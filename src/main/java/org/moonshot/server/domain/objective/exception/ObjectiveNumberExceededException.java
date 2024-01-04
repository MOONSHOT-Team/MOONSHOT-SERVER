package org.moonshot.server.domain.objective.exception;

import org.moonshot.server.global.common.exception.MoonshotException;
import org.moonshot.server.global.common.response.ErrorType;

public class ObjectiveNumberExceededException extends MoonshotException {

    public ObjectiveNumberExceededException() {
        super(ErrorType.ACTIVE_OBJECTIVE_NUMBER_EXCEEDED);
    }

}
