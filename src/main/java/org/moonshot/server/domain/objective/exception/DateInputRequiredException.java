package org.moonshot.server.domain.objective.exception;

import org.moonshot.server.global.common.exception.MoonshotException;
import org.moonshot.server.global.common.response.ErrorType;

public class DateInputRequiredException extends MoonshotException {

    public DateInputRequiredException() {
        super(ErrorType.REQUIRED_EXPIRE_AT);
    }

}
