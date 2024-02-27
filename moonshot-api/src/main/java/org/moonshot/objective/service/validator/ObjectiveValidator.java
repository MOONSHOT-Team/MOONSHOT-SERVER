package org.moonshot.objective.service.validator;

import org.moonshot.exception.global.auth.AccessDeniedException;
import org.moonshot.exception.objective.ObjectiveInvalidIndexException;

public class ObjectiveValidator {

    public static void validateUserAuthorization(Long userEntityId, Long userId) {
        if (!userEntityId.equals(userId)) {
            throw new AccessDeniedException();
        }
    }

    public static void validateIndexWithInRange(final Long objectiveCount, final int idx) {
        if ((objectiveCount <= idx) || (idx < 0)) {
            throw new ObjectiveInvalidIndexException();
        }
    }

    public static boolean isSameIndex(final int prevIdx, final int idx) {
        return prevIdx == idx;
    }

    public static boolean isIndexIncreased(final int prevIdx, final int idx) {
        return prevIdx < idx;
    }

}
