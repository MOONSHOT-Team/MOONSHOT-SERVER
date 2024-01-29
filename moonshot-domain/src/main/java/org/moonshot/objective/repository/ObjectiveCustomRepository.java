package org.moonshot.objective.repository;

import java.util.List;
import org.moonshot.objective.model.Category;
import org.moonshot.objective.model.Criteria;
import org.moonshot.objective.model.Objective;

public interface ObjectiveCustomRepository {

    List<Objective> findObjectives(Long userId, Integer year, Category category, Criteria criteria);

}
