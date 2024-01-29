package org.moonshot.keyresult.repository;

import java.util.List;
import java.util.Optional;
import org.moonshot.keyresult.model.KeyResult;
import org.moonshot.objective.model.Objective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface KeyResultRepository extends JpaRepository<KeyResult, Long> {

    Long countAllByObjective(Objective objective);
    List<KeyResult> findByIdIn(List<Long> ids);
    List<KeyResult> findAllByObjective(Objective objective);
    List<KeyResult> findAllByObjectiveOrderByIdx(Objective objective);
    List<KeyResult> findAllByObjectiveId(Long objectiveId);
    @Query("select count(kr) from KeyResult kr join kr.objective obj where obj.id = :objectiveId")
    Long countAllByObjectiveId(@Param("objectiveId") Long objectiveId);
    @Query("select kr from KeyResult kr join fetch kr.objective join fetch kr.objective.user where kr.id = :keyResultId")
    Optional<KeyResult> findKeyResultAndObjective(@Param("keyResultId") Long keyResultId);
    @Modifying(clearAutomatically = true)
    @Query("UPDATE KeyResult kr SET kr.idx = kr.idx + 1 WHERE kr.idx >= :lBound AND kr.idx < :uBound AND kr.objective.id = :objectiveId AND kr.id != :targetId")
    void bulkUpdateIdxIncrease(@Param("lBound") int lowerBound, @Param("uBound") int upperBound, @Param("objectiveId") Long objectiveId, @Param("targetId") Long targetId);
    @Modifying(clearAutomatically = true)
    @Query("UPDATE KeyResult kr SET kr.idx = kr.idx - 1 WHERE kr.idx >= :lBound AND kr.idx <= :uBound AND kr.objective.id = :objectiveId AND kr.id != :targetId")
    void bulkUpdateIdxDecrease(@Param("lBound") int lowerBound, @Param("uBound") int upperBound, @Param("objectiveId") Long objectiveId, @Param("targetId") Long targetId);

}
