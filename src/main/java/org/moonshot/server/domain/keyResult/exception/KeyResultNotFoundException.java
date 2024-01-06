package org.moonshot.server.domain.keyresult.exception;

import org.moonshot.server.global.common.exception.MoonshotException;
import org.moonshot.server.global.common.response.ErrorType;

public class KeyResultNotFoundException extends MoonshotException {

    public KeyResultNotFoundException() {
        super(ErrorType.NOT_FOUND_KEY_RESULT_ERROR);
    }

}
