package org.moonshot.server.domain.objective.exception;

import org.moonshot.server.global.common.exception.MoonshotException;
import org.moonshot.server.global.common.response.ErrorType;

public class UserObjectiveEmptyException extends MoonshotException {
    public UserObjectiveEmptyException() {
        super(ErrorType.USER_OBJECTIVE_EMPTY);
    }
}
