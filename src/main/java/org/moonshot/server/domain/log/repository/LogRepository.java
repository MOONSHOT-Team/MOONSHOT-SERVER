package org.moonshot.server.domain.log.repository;

import org.moonshot.server.domain.keyresult.model.KeyResult;
import org.moonshot.server.domain.log.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LogRepository extends JpaRepository<Log, Long> {

    List<Log> findAllByKeyResult(KeyResult keyResult);

    @Query("select l FROM Log l JOIN FETCH l.keyResult k WHERE l.keyResult.id = :keyResultId ORDER BY l.id DESC LIMIT 1")
    List<Log> findLatestLogByKeyResultId(@Param("keyResultId") Long keyResultId);

}
