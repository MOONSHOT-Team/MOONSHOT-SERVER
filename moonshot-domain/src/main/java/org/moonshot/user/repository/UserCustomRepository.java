package org.moonshot.user.repository;

import java.util.Optional;
import org.moonshot.user.model.User;

public interface UserCustomRepository {

    Optional<User> findByIdWithCache(Long id);

}
