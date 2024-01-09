package org.moonshot.server.domain.log.repository;

import org.moonshot.server.domain.keyresult.model.KeyResult;
import org.moonshot.server.domain.log.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogRepository extends JpaRepository<Log, Long> {

    List<Log> findAllByKeyResult(KeyResult keyResult);

}
