package org.moonshot.server.domain.log.exception;

import org.moonshot.server.global.common.exception.MoonshotException;
import org.moonshot.server.global.common.response.ErrorType;

public class InvalidLogValueException extends MoonshotException {

    public InvalidLogValueException() { super(ErrorType.INVALID_LOG_VALUE); }
}
