package org.moonshot.server.domain.objective.repository;

import org.moonshot.server.domain.objective.model.Objective;
import org.moonshot.server.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ObjectiveRepository extends JpaRepository<Objective, Long> {

    Long countAllByUserAndIsClosed(User user, boolean isClosed);

}
