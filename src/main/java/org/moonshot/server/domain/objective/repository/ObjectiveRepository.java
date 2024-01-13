package org.moonshot.server.domain.objective.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface ObjectiveRepository extends ObjectiveJpaRepository, ObjectiveCustomRepository {
}
