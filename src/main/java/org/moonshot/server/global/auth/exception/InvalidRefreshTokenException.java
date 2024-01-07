package org.moonshot.server.global.auth.exception;

import org.moonshot.server.global.common.exception.MoonshotException;
import org.moonshot.server.global.common.response.ErrorType;

public class InvalidRefreshTokenException extends MoonshotException {
    public InvalidRefreshTokenException() {
        super(ErrorType.INVALID_REFRESHTOKEN_ERROR);
    }
}