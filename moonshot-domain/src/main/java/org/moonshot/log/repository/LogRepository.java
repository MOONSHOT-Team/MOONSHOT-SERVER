package org.moonshot.log.repository;

import java.util.List;
import java.util.Optional;
import org.moonshot.keyresult.model.KeyResult;
import org.moonshot.log.model.Log;
import org.moonshot.log.model.LogState;
import org.moonshot.objective.model.Objective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LogRepository extends JpaRepository<Log, Long> {

    List<Log> findAllByKeyResult(KeyResult keyResult);
    List<Log> findAllByKeyResultOrderByIdDesc(KeyResult keyResult);
    @Query("select l FROM Log l JOIN FETCH l.keyResult k WHERE l.state = :state and l.keyResult.id = :keyResultId ORDER BY l.id DESC LIMIT 1")
    Optional<Log> findLatestLogByKeyResultId(@Param("state") LogState state, @Param("keyResultId") Long keyResultId);
    List<Log> findAllByKeyResultIn(List<KeyResult> keyResultList);

}
