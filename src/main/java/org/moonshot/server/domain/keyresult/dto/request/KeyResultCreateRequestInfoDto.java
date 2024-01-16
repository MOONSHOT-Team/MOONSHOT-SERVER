package org.moonshot.server.domain.keyresult.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import org.moonshot.server.domain.task.dto.request.TaskCreateRequestDto;
import org.hibernate.validator.constraints.Range;

public record KeyResultCreateRequestInfoDto(
        @Size(min = 1, max = 30, message = "KR은 30자 이하여야 합니다.")
        String title,
        @NotNull(message = "KR 시작 날짜를 선택해주세요.")
        LocalDate startAt,
        @NotNull(message = "KR 종료 날짜를 선택해주세요.")
        LocalDate expireAt,
        @NotNull(message = "KR의 순서를 입력해주세요.")
        @Range(min = 0, max = 2, message = "KeyResult의 순서는 0부터 2까지로 설정할 수 있습니다.")
        Integer idx,
        @NotNull(message = "KR 목표 수치를 입력해주세요.")
        @Range(min = 1, max = 999999L, message = "수치는 999,999 이하여야 합니다.")
        Long target,
        @NotNull(message = "KR 목표 수치의 단위를 입력해주세요.")
        String metric,
        @Valid @Size(max = 3, message = "Task 개수는 최대 3개로 제한되어 있습니다.")
        List<TaskCreateRequestDto> taskList
) {
}
