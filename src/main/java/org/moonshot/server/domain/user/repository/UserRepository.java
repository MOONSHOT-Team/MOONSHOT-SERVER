package org.moonshot.server.domain.user.repository;

import java.util.Optional;
import org.moonshot.server.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByNickname(String nickname);

}
