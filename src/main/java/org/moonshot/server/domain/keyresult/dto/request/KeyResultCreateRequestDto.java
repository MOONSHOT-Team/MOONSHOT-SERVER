package org.moonshot.server.domain.keyresult.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import org.hibernate.validator.constraints.Range;
import org.moonshot.server.global.common.model.validator.ValidLimitValue;
import org.moonshot.server.global.common.model.validator.ValidTargetNumber;
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
        @Range(min = 0, max = 2, message = "KeyResult의 순서는 0부터 2까지로 설정할 수 있습니다.")
        Integer idx,
        @NotNull(message = "KR 목표 수치를 입력해주세요.")
        @ValidTargetNumber
        @ValidLimitValue
        Long target,
        @NotNull(message = "KR 목표 수치의 단위를 입력해주세요.")
        String metric,
        @NotNull(message = "KR 목표의 이전 수식을 입력해주세요.")
        String descriptionBefore,
        @NotNull(message = "KR 목표의 이후 수식을 입력해주세요.")
        String descriptionAfter
) {
}
