package org.moonshot.server.domain.objective.repository;

import java.util.Optional;
import org.moonshot.server.domain.objective.model.Objective;
import org.moonshot.server.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ObjectiveRepository extends JpaRepository<Objective, Long> {

    Long countAllByUserAndIsClosed(User user, boolean isClosed);
    @Query("select o from Objective o join fetch o.user where o.id = :objective_id")
    Optional<Objective> findObjectiveAndUserById(@Param("objective_id") Long objectiveId);

}
