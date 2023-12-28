package org.moonshot.server.global.common.exception;

import lombok.Getter;
import org.moonshot.server.global.common.response.ErrorType;

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