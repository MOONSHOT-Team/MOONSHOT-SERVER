package org.moonshot.server.domain.objective.repository;

import java.util.List;
import java.util.Optional;
import org.moonshot.server.domain.objective.model.Objective;
import org.moonshot.server.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ObjectiveJpaRepository extends JpaRepository<Objective, Long> {

    Long countAllByUserAndIsClosed(User user, boolean isClosed);
    @Query("select o from Objective o join fetch o.user where o.id = :objective_id")
    Optional<Objective> findObjectiveAndUserById(@Param("objective_id") Long objectiveId);
    @Query("select distinct o from Objective o left join fetch o.keyResultList kr left join kr.taskList t where o.id = :objectiveId and o.isClosed = false and kr.id = t.keyResult.id")
    Optional<Objective> findByIdWithKeyResultsAndTasks(@Param("objectiveId") Long objectiveId);
    @Query("select distinct o from Objective o where o.user.id = :userId and o.isClosed = false order by o.id desc")
    List<Objective> findAllByUserId(@Param("userId") Long userId);

}
