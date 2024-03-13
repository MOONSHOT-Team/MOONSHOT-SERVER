package org.moonshot.s3.dto.request;

import org.moonshot.s3.ImageType;

public record GetPresignedUrlRequestDto(ImageType imageType) {
}
