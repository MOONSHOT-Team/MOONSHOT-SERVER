package org.moonshot.server.domain.keyresult.exception;

import org.moonshot.server.global.common.exception.MoonshotException;
import org.moonshot.server.global.common.response.ErrorType;

public class KeyResultRequiredException extends MoonshotException {

    public KeyResultRequiredException() {
        super(ErrorType.REQUIRED_KEY_RESULT_VALUE);
    }

}
