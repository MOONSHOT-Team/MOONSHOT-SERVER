package org.moonshot.server.global.auth.exception;

import org.moonshot.server.global.common.exception.MoonshotException;
import org.moonshot.server.global.common.response.ErrorType;

public class InvalidAuthException extends MoonshotException {

    public InvalidAuthException() {
        super(ErrorType.INVALID_AUTH_ERROR);
    }

}