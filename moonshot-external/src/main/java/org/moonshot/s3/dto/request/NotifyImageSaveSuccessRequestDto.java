package org.moonshot.s3.dto.request;

public record NotifyImageSaveSuccessRequestDto(
        String key,
        String username
) {
}
