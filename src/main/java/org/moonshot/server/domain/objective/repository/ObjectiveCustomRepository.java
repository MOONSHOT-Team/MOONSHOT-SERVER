package org.moonshot.server.domain.objective.repository;

import java.util.List;
import org.moonshot.server.domain.objective.dto.request.ObjectiveHistoryRequestDto;
import org.moonshot.server.domain.objective.dto.response.HistoryResponseDto;

public interface ObjectiveCustomRepository {

    HistoryResponseDto findObjectives(ObjectiveHistoryRequestDto request);

}
