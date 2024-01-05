package org.moonshot.server.domain.keyresult.repository;

import java.util.List;
import org.moonshot.server.domain.keyresult.model.KeyResult;
import org.moonshot.server.domain.objective.model.Objective;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeyResultRepository extends JpaRepository<KeyResult, Long> {

    Long countAllByObjective(Objective objective);
    List<KeyResult> findByIdIn(List<Long> ids);
    List<KeyResult> findAllByObjective(Objective objective);

}
