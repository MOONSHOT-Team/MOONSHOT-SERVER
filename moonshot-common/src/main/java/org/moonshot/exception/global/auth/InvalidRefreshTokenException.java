package org.moonshot.exception.global.auth;

import org.moonshot.exception.global.common.MoonshotException;
import org.moonshot.response.ErrorType;

public class InvalidRefreshTokenException extends MoonshotException {
    public InvalidRefreshTokenException() {
        super(ErrorType.INVALID_REFRESHTOKEN_ERROR);
    }
}