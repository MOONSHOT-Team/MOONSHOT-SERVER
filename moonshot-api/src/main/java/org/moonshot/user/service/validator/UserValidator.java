package org.moonshot.user.service.validator;

import org.moonshot.exception.global.auth.AccessDeniedException;
import org.moonshot.user.model.User;

import java.util.Optional;

public class UserValidator {

    public static void validateUserAuthorization(final Long userEntityId, final Long userId) {
        if (!userEntityId.equals(userId)) {
            throw new AccessDeniedException();
        }
    }

    public static boolean hasChange(final Object object) {
        return (object != null);
    }

    public static boolean isNewUser(final Optional<User> user) {
        return user.isEmpty();
    }

}
