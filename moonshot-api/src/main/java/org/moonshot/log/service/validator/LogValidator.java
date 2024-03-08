package org.moonshot.log.service.validator;

import org.moonshot.exception.BadRequestException;
import org.moonshot.log.model.LogState;
import org.moonshot.response.ErrorType;

public class LogValidator {

    public static void validateLogNum(Long requestNum, Long originNum) {
        if (requestNum.equals(originNum)) {
            throw new BadRequestException(ErrorType.INVALID_LOG_VALUE);
        }
    }

    public static boolean isCreateLog(LogState state) {
        return state == LogState.CREATE;
    }

}
