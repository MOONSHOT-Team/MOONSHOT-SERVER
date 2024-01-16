package org.moonshot.server.domain.keyresult.exception;

import org.moonshot.server.global.common.exception.MoonshotException;
import org.moonshot.server.global.common.response.ErrorType;

public class KeyResultInvalidPositionException extends MoonshotException {

    public KeyResultInvalidPositionException() {
        super(ErrorType.INVALID_KEY_RESULT_INDEX);
    }

}
