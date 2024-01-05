package org.moonshot.server.domain.objective.exception;

import org.moonshot.server.global.common.exception.MoonshotException;
import org.moonshot.server.global.common.response.ErrorType;

public class ObjectiveNotFoundException extends MoonshotException {
    public ObjectiveNotFoundException() {
        super(ErrorType.NOT_FOUND_OBJECTIVE_ERROR);
    }
}
