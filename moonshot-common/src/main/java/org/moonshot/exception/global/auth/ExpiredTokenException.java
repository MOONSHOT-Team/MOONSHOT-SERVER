package org.moonshot.exception.global.auth;

import org.moonshot.exception.global.common.MoonshotException;
import org.moonshot.response.ErrorType;

public class ExpiredTokenException extends MoonshotException {

    public ExpiredTokenException() {
        super(ErrorType.EXPIRED_TOKEN_ERROR);
    }

}