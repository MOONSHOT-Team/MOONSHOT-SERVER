package org.moonshot.discord;


import java.time.LocalDateTime;

public record SignUpEvent(String name, String email, String socialPlatform, LocalDateTime createdAt, String imageUrl) {

    public static SignUpEvent of(String name, String email, String socialPlatform, LocalDateTime createdAt,
                                 String imageUrl) {
        return new SignUpEvent(name, email, socialPlatform, createdAt, imageUrl);
    }

}
