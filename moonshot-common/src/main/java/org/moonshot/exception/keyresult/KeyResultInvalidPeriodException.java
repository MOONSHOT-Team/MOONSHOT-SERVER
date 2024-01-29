package org.moonshot.exception.keyresult;


import org.moonshot.exception.global.common.MoonshotException;
import org.moonshot.response.ErrorType;

public class KeyResultInvalidPeriodException extends MoonshotException {
    public KeyResultInvalidPeriodException() {
        super(ErrorType.INVALID_KEY_RESULT_PERIOD);
    }
}
