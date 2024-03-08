package org.moonshot.keyresult.service.validator;

import static org.moonshot.response.ErrorType.ACTIVE_KEY_RESULT_NUMBER_EXCEEDED;
import static org.moonshot.response.ErrorType.INVALID_KEY_RESULT_INDEX;
import static org.moonshot.response.ErrorType.INVALID_KEY_RESULT_PERIOD;

import java.time.LocalDate;
import java.util.List;
import org.moonshot.common.model.Period;
import org.moonshot.exception.BadRequestException;

public class KeyResultValidator {

    private static final int ACTIVE_KEY_RESULT_NUMBER = 3;

    public static void validateKeyResultIndex(final int index, final int requestIndex) {
        if (index != requestIndex) {
            throw new BadRequestException(INVALID_KEY_RESULT_INDEX);
        }
    }

    public static void validateKRPeriodWithInObjPeriod(final Period objPeriod, final LocalDate start, final LocalDate end) {
        if (start.isBefore(objPeriod.getStartAt()) || end.isAfter(objPeriod.getExpireAt())
            || start.isAfter(objPeriod.getExpireAt()) || start.isAfter(end)) {
            throw new BadRequestException(INVALID_KEY_RESULT_PERIOD);
        }
    }

    public static void validateActiveKRSizeExceeded(final int krListSize) {
        if (krListSize >= ACTIVE_KEY_RESULT_NUMBER) {
            throw new BadRequestException(ACTIVE_KEY_RESULT_NUMBER_EXCEEDED);
        }
    }

    public static void validateIndexUnderMaximum(final int requestIndex, final int totalKRListSize) {
        if (requestIndex > totalKRListSize) {
            throw new BadRequestException(INVALID_KEY_RESULT_INDEX);
        }
    }

    public static void validateKeyResultPeriod(final Period objPeriod, final LocalDate start, final LocalDate end) {
        if (start.isAfter(end) || start.isBefore(objPeriod.getStartAt()) || start.isAfter(objPeriod.getExpireAt())
            || end.isBefore(start) || end.isBefore(objPeriod.getStartAt()) || end.isAfter(objPeriod.getExpireAt())) {
            throw new BadRequestException(INVALID_KEY_RESULT_PERIOD);
        }
    }

    public static void validateIndex(final Long keyResultCount, final Integer requestIndex) {
        if (keyResultCount <= requestIndex || requestIndex < 0) {
            throw new BadRequestException(INVALID_KEY_RESULT_INDEX);
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
