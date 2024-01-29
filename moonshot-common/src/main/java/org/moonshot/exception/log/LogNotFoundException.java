package org.moonshot.exception.log;

import org.moonshot.exception.global.common.MoonshotException;
import org.moonshot.response.ErrorType;

public class LogNotFoundException extends MoonshotException {

    public LogNotFoundException() {
        super(ErrorType.NOT_FOUND_LOG_ERROR);
    }

}
