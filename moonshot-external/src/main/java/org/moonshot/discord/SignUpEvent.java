package org.moonshot.discord;


import java.time.LocalDateTime;

public record SignUpEvent(Long totalUserCount, String name, String email, String socialPlatform, LocalDateTime createdAt, String imageUrl) {

    public static SignUpEvent of(Long totalUserCount, String name, String email, String socialPlatform, LocalDateTime createdAt,
                                 String imageUrl) {
        return new SignUpEvent(totalUserCount, name, email, socialPlatform, createdAt, imageUrl);
    }

}
