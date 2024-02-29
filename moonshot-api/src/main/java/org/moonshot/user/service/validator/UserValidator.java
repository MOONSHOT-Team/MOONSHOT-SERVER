package org.moonshot.user.service.validator;

import org.moonshot.exception.global.auth.AccessDeniedException;

public class UserValidator {

    public static void validateUserAuthorization(final Long userEntityId, final Long userId) {
        if (!userEntityId.equals(userId)) {
            throw new AccessDeniedException();
        }
    }

}
