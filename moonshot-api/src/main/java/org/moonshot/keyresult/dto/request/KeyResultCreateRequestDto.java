package org.moonshot.keyresult.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

public record KeyResultCreateRequestDto(
        @NotNull(message = "KR이 생성될 ObjectiveId를 입력해주세요.")
        Long objectiveId,
        @Size(min = 1, max = 30, message = "KR은 30자 이하여야 합니다.")
        String krTitle,
        @NotNull(message = "KR 시작 날짜를 선택해주세요.")
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDate krStartAt,
        @NotNull(message = "KR 종료 날짜를 선택해주세요.")
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDate krExpireAt,
        @NotNull(message = "KR의 순서를 입력해주세요.")
        @Range(min = 0, max = 2, message = "KeyResult의 순서는 0부터 2까지로 설정할 수 있습니다.")
        Integer krIdx,
        @NotNull(message = "KR 목표 수치를 입력해주세요.")
        @Range(min = 1, max = 999999L, message = "수치는 999,999 이하여야 합니다.")
        Long krTarget,
        @NotNull(message = "KR 목표 수치의 단위를 입력해주세요.")
        String krMetric
) {
}
