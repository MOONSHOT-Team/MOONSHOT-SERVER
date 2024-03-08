package org.moonshot.exception;

import org.moonshot.response.ErrorType;

public class ForbiddenException extends MoonshotException {

    public ForbiddenException() {
        super(ErrorType.FORBIDDEN);
    }

    public ForbiddenException(ErrorType errorType) {
        super(errorType);
    }
}
