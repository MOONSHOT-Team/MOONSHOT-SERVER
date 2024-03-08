package org.moonshot.exception;

import lombok.Getter;
import org.moonshot.response.ErrorType;

@Getter
public class MoonshotException extends RuntimeException {

    private final ErrorType errorType;

    public MoonshotException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }

    public int getHttpStatus() {
        return errorType.getHttpStatusCode();
    }
}