package org.moonshot.server.domain.objective.exception;

import org.moonshot.server.global.common.exception.MoonshotException;
import org.moonshot.server.global.common.response.ErrorType;

public class LogRequiredException extends MoonshotException {

    public LogRequiredException() {
        super(ErrorType.REQUIRED_LOG_VALUE);
    }

}
