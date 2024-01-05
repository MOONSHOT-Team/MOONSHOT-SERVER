package org.moonshot.server.domain.user.exception;

import org.moonshot.server.global.common.exception.MoonshotException;
import org.moonshot.server.global.common.response.ErrorType;

public class UserNotFoundException extends MoonshotException {
    public UserNotFoundException() {
        super(ErrorType.NOT_FOUND_USER_ERROR);
    }
}
