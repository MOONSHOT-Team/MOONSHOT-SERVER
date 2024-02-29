package org.moonshot.objective.service.validator;

import org.moonshot.exception.objective.ObjectiveInvalidIndexException;
import org.moonshot.exception.objective.ObjectiveNumberExceededException;

public class ObjectiveValidator {

    private static final int ACTIVE_OBJECTIVE_NUMBER = 10;

    public static void validateIndexWithInRange(final Long objectiveCount, final int idx) {
        if ((objectiveCount <= idx) || (idx < 0)) {
            throw new ObjectiveInvalidIndexException();
        }
    }

    public static void validateActiveObjectiveSizeExceeded(final int objListSize) {
        if (objListSize >= ACTIVE_OBJECTIVE_NUMBER) {
            throw new ObjectiveNumberExceededException();
        }
    }

}
