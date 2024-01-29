package org.moonshot.exception.objective;

import org.moonshot.exception.global.common.MoonshotException;
import org.moonshot.response.ErrorType;

public class ObjectiveInvalidIndexException extends MoonshotException {
    public ObjectiveInvalidIndexException() {
        super(ErrorType.INVALID_OBJECTIVE_INDEX);
    }
}
