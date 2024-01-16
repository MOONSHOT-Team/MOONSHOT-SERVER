package org.moonshot.server.domain.keyresult.exception;

import org.moonshot.server.global.common.exception.MoonshotException;
import org.moonshot.server.global.common.response.ErrorType;

public class KeyResultInvalidIndexException extends MoonshotException {

    public KeyResultInvalidIndexException() {
        super(ErrorType.INVALID_KEY_RESULT_INDEX);
    }

}
