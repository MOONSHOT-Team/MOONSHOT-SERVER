package org.moonshot.exception;

import org.moonshot.response.ErrorType;

public class NotFoundException extends MoonshotException {

    public NotFoundException() {
        super(ErrorType.NOT_FOUND);
    }

    public NotFoundException(ErrorType errorType) {
        super(errorType);
    }

}
