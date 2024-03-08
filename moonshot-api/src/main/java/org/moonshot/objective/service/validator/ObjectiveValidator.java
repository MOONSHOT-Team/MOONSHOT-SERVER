package org.moonshot.objective.service.validator;

import static org.moonshot.response.ErrorType.ACTIVE_OBJECTIVE_NUMBER_EXCEEDED;
import static org.moonshot.response.ErrorType.INVALID_OBJECTIVE_INDEX;

import org.moonshot.exception.BadRequestException;

public class ObjectiveValidator {

    private static final int ACTIVE_OBJECTIVE_NUMBER = 10;

    public static void validateIndexWithInRange(final Long objectiveCount, final int idx) {
        if ((objectiveCount <= idx) || (idx < 0)) {
            throw new BadRequestException(INVALID_OBJECTIVE_INDEX);
        }
    }

    public static void validateActiveObjectiveSizeExceeded(final int objListSize) {
        if (objListSize >= ACTIVE_OBJECTIVE_NUMBER) {
            throw new BadRequestException(ACTIVE_OBJECTIVE_NUMBER_EXCEEDED);
        }
    }

}
