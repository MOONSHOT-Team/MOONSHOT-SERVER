package org.moonshot.server.domain.keyresult.exception;

import org.moonshot.server.global.common.exception.MoonshotException;
import org.moonshot.server.global.common.response.ErrorType;

public class KeyResultNumberExceededException extends MoonshotException {

    public KeyResultNumberExceededException() {
        super(ErrorType.ACTIVE_KEY_RESULT_NUMBER_EXCEEDED);
    }

}
