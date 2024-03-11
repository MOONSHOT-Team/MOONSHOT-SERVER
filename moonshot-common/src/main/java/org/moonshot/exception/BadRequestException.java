package org.moonshot.exception;

import org.moonshot.response.ErrorType;

public class BadRequestException extends MoonshotException {

    public BadRequestException() {
        super(ErrorType.BAD_REQUEST);
    }

    public BadRequestException(ErrorType errorType) {
        super(errorType);
    }

}
