package org.moonshot.server.domain.log.exception;

import org.moonshot.server.global.common.exception.MoonshotException;
import org.moonshot.server.global.common.response.ErrorType;

public class LogNotFoundException extends MoonshotException {

    public LogNotFoundException() {
        super(ErrorType.NOT_FOUND_LOG_ERROR);
    }

}
