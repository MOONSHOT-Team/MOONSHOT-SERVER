package org.moonshot.server.domain.objective.dto.response;

import java.util.List;
import org.moonshot.server.domain.keyresult.dto.response.KeyResultResponseDto;
import org.moonshot.server.domain.objective.model.Objective;

public record DashboardResponseDto(
        TreeResponseDto tree,
        List<ObjectiveResponseDto> objList,
        String nickname
) {
    public static DashboardResponseDto of(Objective objective, List<Objective> objList, String nickname) {
        return new DashboardResponseDto(
                TreeResponseDto.of(objective, objList.size(), KeyResultResponseDto.of(objective.getKeyResultList())),
                objList.stream().map(ObjectiveResponseDto::of).toList(),
                nickname
        );
    }

    public static DashboardResponseDto ofNull(String nickname) {
        return new DashboardResponseDto(
                null,
                null,
                nickname

        );
    }
}
