package org.moonshot.server.global.auth.exception;

import org.moonshot.server.global.common.exception.MoonshotException;
import org.moonshot.server.global.common.response.ErrorType;

public class ExpiredTokenException extends MoonshotException {

    public ExpiredTokenException() {
        super(ErrorType.EXPIRED_TOKEN_ERROR);
    }

}