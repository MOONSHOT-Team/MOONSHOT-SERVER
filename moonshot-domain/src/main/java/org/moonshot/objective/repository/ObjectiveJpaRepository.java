package org.moonshot.objective.repository;

import java.util.List;
import java.util.Optional;
import org.moonshot.objective.model.Objective;
import org.moonshot.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ObjectiveJpaRepository extends JpaRepository<Objective, Long> {

    Long countAllByUserAndIsClosed(User user, boolean isClosed);
    @Query("select o from Objective o join fetch o.user where o.id = :objective_id")
    Optional<Objective> findObjectiveAndUserById(@Param("objective_id") Long objectiveId);
    @Query("select distinct o from Objective o left join fetch o.keyResultList kr left join kr.taskList t where o.id = :objectiveId and o.isClosed = false")
    Optional<Objective> findByIdWithKeyResultsAndTasks(@Param("objectiveId") Long objectiveId);
    @Query("select distinct o from Objective o join fetch o.user where o.user.id = :userId and o.isClosed = false order by o.idx asc")
    List<Objective> findAllByUserId(@Param("userId") Long userId);
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Objective o SET o.idx = o.idx + 1 WHERE o.user.id = :userId")
    void bulkUpdateIdxIncrease(@Param("userId") Long userId);
    Long countAllByUserId(Long userId);
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Objective o SET o.idx = o.idx + 1 WHERE o.idx >= :lBound AND o.idx < :uBound AND o.user.id = :userId AND o.id != :targetId")
    void bulkUpdateIdxIncrease(@Param("lBound") int lowerBound, @Param("uBound") int upperBound, @Param("userId") Long userId, @Param("targetId") Long targetId);
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Objective o SET o.idx = o.idx - 1 WHERE o.idx >= :lBound AND o.idx <= :uBound AND o.user.id = :userId AND o.id != :targetId")
    void bulkUpdateIdxDecrease(@Param("lBound") int lowerBound, @Param("uBound") int upperBound, @Param("userId") Long userId, @Param("targetId") Long targetId);
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Objective  o SET o.idx = o.idx - 1 WHERE o.idx >= :lBound AND o.user.id = :userId")
    void bulkUpdateIdxDecreaseAfter(@Param("lBound") int lowerBound, @Param("userId") Long userId);
    List<Objective> findAllByUserIn(List<User> userList);

}
