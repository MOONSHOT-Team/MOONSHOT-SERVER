package org.moonshot.exception;

import org.moonshot.response.ErrorType;

public class UnauthorizedException extends MoonshotException {

    public UnauthorizedException() {
        super(ErrorType.UNAUTHORIZED);
    }

    public UnauthorizedException(ErrorType errorType) {
        super(errorType);
    }

}
