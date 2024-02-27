package org.moonshot.objective.dto.response;

import java.util.List;
import org.moonshot.keyresult.dto.response.KeyResultResponseDto;
import org.moonshot.objective.model.Objective;

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
