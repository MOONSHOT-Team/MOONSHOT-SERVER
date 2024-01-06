package org.moonshot.server.domain.user.repository;

import org.moonshot.server.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserBySocialId(String socialId);
    Optional<User> findUserByNickname(String nickname);
}

