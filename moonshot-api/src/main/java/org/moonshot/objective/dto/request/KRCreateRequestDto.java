package org.moonshot.objective.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import org.hibernate.validator.constraints.Range;
import org.moonshot.task.dto.request.TaskCreateRequestDto;

public record KRCreateRequestDto(
        @Size(min = 1, max = 30, message = "KR은 30자 이하여야 합니다.")
        String title,
        @NotNull(message = "KR 시작 날짜를 선택해주세요.")
        LocalDate startAt,
        @NotNull(message = "KR 종료 날짜를 선택해주세요.")
        LocalDate expireAt,
        @NotNull(message = "KR 목표 수치를 입력해주세요.")
        @Range(min = 0, max = 999999L)
        Integer target,
        @NotNull(message = "KR 목표 수치의 단위를 입력해주세요.")
        String metric,
        @NotNull(message = "KR 목표의 이전 수식을 입력해주세요.")
        String descriptionBefore,
        @NotNull(message = "KR 목표의 이후 수식을 입력해주세요.")
        String descriptionAfter,
        @Valid @Size(max = 3, message = "Task 개수는 최대 3개로 제한되어 있습니다.")
        List<TaskCreateRequestDto> taskList
) {
}
