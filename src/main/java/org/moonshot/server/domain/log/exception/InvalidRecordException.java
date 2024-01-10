package org.moonshot.server.domain.log.exception;

import org.moonshot.server.global.common.exception.MoonshotException;
import org.moonshot.server.global.common.response.ErrorType;

public class InvalidRecordException extends MoonshotException {

    public InvalidRecordException() { super(ErrorType.INVALID_RECORD_VALUE); }

}
