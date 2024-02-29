package org.moonshot.keyresult.service.validator;

import java.time.LocalDate;
import java.util.List;
import org.moonshot.common.model.Period;
import org.moonshot.exception.global.auth.AccessDeniedException;
import org.moonshot.exception.keyresult.KeyResultInvalidIndexException;
import org.moonshot.exception.keyresult.KeyResultInvalidPeriodException;
import org.moonshot.exception.keyresult.KeyResultNumberExceededException;
import org.moonshot.exception.log.InvalidLogValueException;

public class KeyResultValidator {

    private static final int ACTIVE_KEY_RESULT_NUMBER = 3;

    public static void validateKeyResultIndex(final int index, final int requestIndex) {
        if (index != requestIndex) {
            throw new KeyResultInvalidIndexException();
        }
    }

    public static void validateKRPeriodWithInObjPeriod(final Period objPeriod, final LocalDate start, final LocalDate end) {
        if (start.isBefore(objPeriod.getStartAt()) || end.isAfter(objPeriod.getExpireAt())
            || start.isAfter(objPeriod.getExpireAt()) || start.isAfter(end)) {
            throw new KeyResultInvalidPeriodException();
        }
    }

    public static void validateActiveKRSizeExceeded(final int krListSize) {
        if (krListSize >= ACTIVE_KEY_RESULT_NUMBER) {
            throw new KeyResultNumberExceededException();
        }
    }

    public static void validateIndexUnderMaximum(final int requestIndex, final int totalKRListSize) {
        if (requestIndex > totalKRListSize) {
            throw new KeyResultInvalidIndexException();
        }
    }

    public static void validateLogValue(Long requestTarget, Long originTarget) {
        if (requestTarget.equals(originTarget)) {
            throw new InvalidLogValueException();
        }
    }

    public static void validateKeyResultPeriod(final Period objPeriod, final LocalDate start, final LocalDate end) {
        if (start.isAfter(end) || start.isBefore(objPeriod.getStartAt()) || start.isAfter(objPeriod.getStartAt())
            || end.isBefore(start) || end.isBefore(objPeriod.getStartAt()) || end.isAfter(objPeriod.getExpireAt())) {
            throw new KeyResultInvalidPeriodException();
        }
    }

    public static void validateIndex(final Long keyResultCount, final Integer requestIndex) {
        if (keyResultCount <= requestIndex || requestIndex < 0) {
            throw new KeyResultInvalidIndexException();
        }
    }

    public static boolean hasKeyResultTask(final List<?> taskList) {
        return taskList != null;
    }

    public static boolean hasChange(final Object object) {
        return (object != null);
    }

    public static boolean hasDateChange(final LocalDate... dates) {
        for (LocalDate date : dates) {
            if (date != null) return true;
        }
        return false;
    }

    public static boolean isKeyResultAchieved(short progress) {
        return progress == 100;
    }

}
