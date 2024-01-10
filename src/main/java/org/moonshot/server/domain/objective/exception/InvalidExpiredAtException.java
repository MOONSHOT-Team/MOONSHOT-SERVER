package org.moonshot.server.domain.objective.exception;

import org.moonshot.server.global.common.exception.MoonshotException;
import org.moonshot.server.global.common.response.ErrorType;

public class InvalidExpiredAtException extends MoonshotException {

    public InvalidExpiredAtException() {
        super(ErrorType.INVALID_EXPIRE_AT);
    }

}
