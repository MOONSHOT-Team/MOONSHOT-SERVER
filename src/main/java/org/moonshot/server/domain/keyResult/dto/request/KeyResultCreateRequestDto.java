package org.moonshot.server.domain.keyresult.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

public record KeyResultCreateRequestDto(
        @NotNull(message = "KR이 생성될 ObjectiveId를 입력해주세요.")
        Long objectiveId,
        @Size(min = 1, max = 30, message = "KR은 30자 이하여야 합니다.")
        String title,
        @NotNull(message = "KR 시작 날짜를 선택해주세요.")
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime startAt,
        @NotNull(message = "KR 종료 날짜를 선택해주세요.")
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime expireAt,
        @NotNull(message = "KR의 순서를 입력해주세요.")
        short order,
        @NotNull(message = "KR 목표 수치를 입력해주세요.")
        int target,
        @NotNull(message = "KR 목표 수치의 단위를 입력해주세요.")
        String metric,
        @NotNull(message = "KR 목표의 이전 수식을 입력해주세요.")
        String descriptionBefore,
        @NotNull(message = "KR 목표의 이후 수식을 입력해주세요.")
        String descriptionAfter
) {
}
