package org.moonshot.server.domain.objective.exception;

import org.moonshot.server.global.common.exception.MoonshotException;
import org.moonshot.server.global.common.response.ErrorType;

public class ObjectiveInvalidIndexException extends MoonshotException {
    public ObjectiveInvalidIndexException() {
        super(ErrorType.INVALID_OBJECTIVE_INDEX);
    }
}
