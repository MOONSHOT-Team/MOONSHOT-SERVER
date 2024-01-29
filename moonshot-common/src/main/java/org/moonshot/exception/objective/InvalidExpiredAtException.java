package org.moonshot.exception.objective;

import org.moonshot.exception.global.common.MoonshotException;
import org.moonshot.response.ErrorType;

public class InvalidExpiredAtException extends MoonshotException {

    public InvalidExpiredAtException() {
        super(ErrorType.INVALID_EXPIRE_AT);
    }

}
