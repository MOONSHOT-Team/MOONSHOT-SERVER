package org.moonshot.exception.global.auth;

import org.moonshot.exception.global.common.MoonshotException;
import org.moonshot.response.ErrorType;

public class AccessDeniedException extends MoonshotException {
    public AccessDeniedException() {
        super(ErrorType.FORBIDDEN_ERROR);
    }
}
