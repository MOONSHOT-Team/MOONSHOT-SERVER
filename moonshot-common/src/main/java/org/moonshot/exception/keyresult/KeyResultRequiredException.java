package org.moonshot.exception.keyresult;

import org.moonshot.exception.global.common.MoonshotException;
import org.moonshot.response.ErrorType;

public class KeyResultRequiredException extends MoonshotException {

    public KeyResultRequiredException() {
        super(ErrorType.REQUIRED_KEY_RESULT_VALUE);
    }

}
