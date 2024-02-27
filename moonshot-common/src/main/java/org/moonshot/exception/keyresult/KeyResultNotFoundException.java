package org.moonshot.exception.keyresult;

import org.moonshot.exception.global.common.MoonshotException;
import org.moonshot.response.ErrorType;

public class KeyResultNotFoundException extends MoonshotException {

    public KeyResultNotFoundException() {
        super(ErrorType.NOT_FOUND_KEY_RESULT_ERROR);
    }

}
