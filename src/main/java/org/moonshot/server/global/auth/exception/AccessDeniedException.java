package org.moonshot.server.global.auth.exception;

import org.moonshot.server.global.common.exception.MoonshotException;
import org.moonshot.server.global.common.response.ErrorType;

public class AccessDeniedException extends MoonshotException {
    public AccessDeniedException() {
        super(ErrorType.FORBIDDEN_ERROR);
    }
}
