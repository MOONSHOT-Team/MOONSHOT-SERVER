package org.moonshot.server.domain.keyresult.repository;

import java.util.List;
import java.util.Optional;
import org.moonshot.server.domain.keyresult.model.KeyResult;
import org.moonshot.server.domain.objective.model.Objective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface KeyResultRepository extends JpaRepository<KeyResult, Long> {

    Long countAllByObjective(Objective objective);
    List<KeyResult> findByIdIn(List<Long> ids);
    List<KeyResult> findAllByObjective(Objective objective);

    @Query("select kr from KeyResult kr join fetch kr.objective where kr.id = :keyResultId")
    Optional<KeyResult> findKeyResultAndObjective(@Param("keyResultId") Long keyResultId);

}
