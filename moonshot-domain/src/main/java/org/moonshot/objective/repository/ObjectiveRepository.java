package org.moonshot.objective.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface ObjectiveRepository extends ObjectiveJpaRepository, ObjectiveCustomRepository {
}
