package org.moonshot.server.global.external.s3.dto.request;

public record NotifyImageSaveSuccessRequestDto(
        String key,
        String username
) {
}
