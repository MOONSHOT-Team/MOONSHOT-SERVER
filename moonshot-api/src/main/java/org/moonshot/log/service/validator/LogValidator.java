package org.moonshot.log.service.validator;

import org.moonshot.exception.log.InvalidLogValueException;
import org.moonshot.log.model.LogState;

public class LogValidator {

    public static void validateLogNum(Long requestNum, Long originNum) {
        if (requestNum.equals(originNum)) {
            throw new InvalidLogValueException();
        }
    }

    public static boolean isCreateLog(LogState state) {
        return state == LogState.CREATE;
    }

}
