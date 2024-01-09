package org.moonshot.server.domain.log.repository;

import org.moonshot.server.domain.log.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> {
}
