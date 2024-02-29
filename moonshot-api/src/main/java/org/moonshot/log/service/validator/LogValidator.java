package org.moonshot.log.service.validator;

import org.moonshot.exception.log.InvalidLogValueException;

public class LogValidator {

    public static void validateLogNum(Long requestNum, Long originNum) {
        if (requestNum.equals(originNum)) {
            throw new InvalidLogValueException();
        }
    }

}
