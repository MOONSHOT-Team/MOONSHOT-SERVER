package org.moonshot.exception.user;

import org.moonshot.exception.global.common.MoonshotException;
import org.moonshot.response.ErrorType;

public class UnauthorizedException extends MoonshotException {

    public UnauthorizedException() {
        super(ErrorType.INVALID_AUTH_ERROR);
    }

}
