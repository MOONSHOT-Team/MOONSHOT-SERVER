package org.moonshot.s3;

public record ImageEvent(Long userId, String imageUrl, ImageType imageType) {

    public static ImageEvent of(Long userId, String imageUrl, ImageType imageType) {
        return new ImageEvent(userId, imageUrl, imageType);
    }

}
