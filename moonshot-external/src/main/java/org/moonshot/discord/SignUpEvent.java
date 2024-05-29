package org.moonshot.discord;


import java.time.LocalDateTime;

public record SignUpEvent(Long totalUsers, String name, String email, String socialPlatform, LocalDateTime createdAt, String imageUrl) {

    public static SignUpEvent of(Long totalUsers, String name, String email, String socialPlatform, LocalDateTime createdAt,
                                 String imageUrl) {
        return new SignUpEvent(totalUsers, name, email, socialPlatform, createdAt, imageUrl);
    }

}
