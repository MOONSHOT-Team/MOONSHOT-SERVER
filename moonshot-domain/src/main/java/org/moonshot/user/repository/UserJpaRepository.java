package org.moonshot.user.repository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.moonshot.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    Optional<User> findUserBySocialId(String socialId);
    Optional<User> findUserByNickname(String nickname);
    @Query("SELECT u FROM User u WHERE u.deleteAt < :currentDate")
    List<User> findIdByDeletedAtBefore(LocalDateTime currentDate);

}

