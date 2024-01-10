package org.moonshot.server.domain.objective.dto.response;

import java.util.List;
import org.moonshot.server.domain.keyresult.dto.response.KeyResultResponseDto;
import org.moonshot.server.domain.objective.model.Objective;

public record DashboardResponseDto(
        Long objId,
        String objTitle,
        int objListSize,
        List<KeyResultResponseDto> krList,
        List<ObjectiveResponseDto> objList
) {
    public static DashboardResponseDto of(Objective objective, List<Objective> objList) {
        return new DashboardResponseDto(
                objective.getId(),
                objective.getTitle(),
                objList.size(),
                KeyResultResponseDto.of(objective.getKeyResultList()),
                objList.stream().map(ObjectiveResponseDto::of).toList()
        );
    }
}
