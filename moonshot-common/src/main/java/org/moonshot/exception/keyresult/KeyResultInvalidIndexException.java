package org.moonshot.exception.keyresult;


import org.moonshot.exception.global.common.MoonshotException;
import org.moonshot.response.ErrorType;

public class KeyResultInvalidIndexException extends MoonshotException {

    public KeyResultInvalidIndexException() {
        super(ErrorType.INVALID_KEY_RESULT_INDEX);
    }

}
