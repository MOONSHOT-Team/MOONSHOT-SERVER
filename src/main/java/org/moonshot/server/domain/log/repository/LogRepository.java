package org.moonshot.server.domain.log.repository;

import org.moonshot.server.domain.keyresult.model.KeyResult;
import org.moonshot.server.domain.log.model.Log;
import org.moonshot.server.domain.log.model.LogState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LogRepository extends JpaRepository<Log, Long> {

    List<Log> findAllByKeyResult(KeyResult keyResult);
    List<Log> findAllByKeyResultOrderByIdDesc(KeyResult keyResult);

    @Query("select l FROM Log l JOIN FETCH l.keyResult k WHERE l.state = :state and l.keyResult.id = :keyResultId ORDER BY l.id DESC LIMIT 1")
    Optional<Log> findLatestLogByKeyResultId(@Param("state") LogState state, @Param("keyResultId") Long keyResultId);

}
