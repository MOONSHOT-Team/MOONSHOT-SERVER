package org.moonshot.server.domain.objective.repository;

import java.util.List;
import org.moonshot.server.domain.objective.dto.request.ObjectiveHistoryRequestDto;
import org.moonshot.server.domain.objective.dto.response.HistoryResponseDto;
import org.moonshot.server.domain.objective.model.Objective;

public interface ObjectiveCustomRepository {

    List<Objective> findObjectives(Long userId, ObjectiveHistoryRequestDto request);

}
