package org.moonshot.user.repository;


import java.util.Optional;
import org.moonshot.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserBySocialId(String socialId);
    Optional<User> findUserByNickname(String nickname);

}

