package org.moonshot.exception.log;

import org.moonshot.exception.global.common.MoonshotException;
import org.moonshot.response.ErrorType;

public class InvalidLogValueException extends MoonshotException {

    public InvalidLogValueException() {
        super(ErrorType.INVALID_LOG_VALUE);
    }
}
