package org.moonshot.exception.user;

import org.moonshot.exception.global.common.MoonshotException;
import org.moonshot.response.ErrorType;

public class UserNotFoundException extends MoonshotException {
    public UserNotFoundException() {
        super(ErrorType.NOT_FOUND_USER_ERROR);
    }
}
