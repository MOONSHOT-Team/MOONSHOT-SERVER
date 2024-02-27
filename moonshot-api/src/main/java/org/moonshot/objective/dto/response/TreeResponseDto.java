package org.moonshot.objective.dto.response;

import java.time.LocalDate;
import java.util.List;
import org.moonshot.keyresult.dto.response.KeyResultResponseDto;
import org.moonshot.objective.model.Objective;

public record TreeResponseDto(
        Long objId,
        String objTitle,
        boolean objIsExpired,
        LocalDate objStartAt,
        LocalDate objExpireAt,
        int objListSize,
        List<KeyResultResponseDto> krList
) {
    public static TreeResponseDto of(Objective objective, int objListSize, List<KeyResultResponseDto> krList) {
        return new TreeResponseDto(
                objective.getId(),
                objective.getTitle(),
                !objective.isClosed() && objective.getPeriod().getExpireAt().isBefore(LocalDate.now()),
                objective.getPeriod().getStartAt(),
                objective.getPeriod().getExpireAt(),
                objListSize,
                krList
        );
    }
}
