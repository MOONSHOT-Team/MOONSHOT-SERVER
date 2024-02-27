package org.moonshot.exception.keyresult;


import org.moonshot.exception.global.common.MoonshotException;
import org.moonshot.response.ErrorType;

public class KeyResultNumberExceededException extends MoonshotException {

    public KeyResultNumberExceededException() {
        super(ErrorType.ACTIVE_KEY_RESULT_NUMBER_EXCEEDED);
    }

}
