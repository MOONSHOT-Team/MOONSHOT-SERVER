package org.moonshot.server.global.external.s3.dto.response;

public record PresignedUrlVO(
        String fileName,
        String url
) {

    public static PresignedUrlVO of(String fileName, String url) {
        return new PresignedUrlVO(fileName, url);
    }

}
