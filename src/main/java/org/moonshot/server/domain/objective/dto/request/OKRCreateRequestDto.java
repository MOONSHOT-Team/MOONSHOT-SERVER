package org.moonshot.server.domain.objective.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import org.moonshot.server.domain.objective.model.Category;
import org.springframework.format.annotation.DateTimeFormat;

public record OKRCreateRequestDto(
        @Size(min = 1, max = 30, message = "제목은 30자 이하여야 합니다.")
        String objTitle,
        @NotNull(message = "카테고리를 선택해야 합니다.")
        Category objCategory,
        @Size(min = 1, max = 100, message = "본문은 100자 이하여야 합니다.")
        String objContent,
        @NotNull(message = "목표 시작 날짜를 선택해주세요.")
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime objStartAt,
        @NotNull(message = "목표 종료 날짜를 선택해주세요.")
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime objExpireAt,
        @NotNull @Valid @Size(min = 1, max = 3, message = "KR 개수는 1~3개로 제한되어 있습니다.")
        List<KRCreateRequestDto> krList
) {
}
