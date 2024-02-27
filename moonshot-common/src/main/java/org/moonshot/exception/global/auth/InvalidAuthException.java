package org.moonshot.exception.global.auth;

import org.moonshot.exception.global.common.MoonshotException;
import org.moonshot.response.ErrorType;

public class InvalidAuthException extends MoonshotException {

    public InvalidAuthException() {
        super(ErrorType.INVALID_AUTH_ERROR);
    }

}