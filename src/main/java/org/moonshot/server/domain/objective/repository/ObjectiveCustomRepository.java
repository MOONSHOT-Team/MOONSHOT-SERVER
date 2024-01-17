package org.moonshot.server.domain.objective.repository;

import java.util.List;
import org.moonshot.server.domain.objective.model.Category;
import org.moonshot.server.domain.objective.model.Criteria;
import org.moonshot.server.domain.objective.model.Objective;

public interface ObjectiveCustomRepository {

    List<Objective> findObjectives(Long userId, Integer year, Category category, Criteria criteria);

}
