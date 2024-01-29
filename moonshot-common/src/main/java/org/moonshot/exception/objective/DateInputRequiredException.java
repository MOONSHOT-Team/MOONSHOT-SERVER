package org.moonshot.exception.objective;

import org.moonshot.exception.global.common.MoonshotException;
import org.moonshot.response.ErrorType;

public class DateInputRequiredException extends MoonshotException {

    public DateInputRequiredException() {
        super(ErrorType.REQUIRED_EXPIRE_AT);
    }

}
