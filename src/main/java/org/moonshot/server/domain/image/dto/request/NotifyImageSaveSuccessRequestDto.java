package org.moonshot.server.domain.image.dto.request;

public record NotifyImageSaveSuccessRequestDto(
        String key,
        String username
) {
}
