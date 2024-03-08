package org.moonshot.exception;

import org.moonshot.response.ErrorType;

public class InternalServerException extends MoonshotException {

    public InternalServerException() {
        super(ErrorType.INTERNAL_SERVER);
    }

    public InternalServerException(ErrorType errorType) {
        super(errorType);
    }

}
