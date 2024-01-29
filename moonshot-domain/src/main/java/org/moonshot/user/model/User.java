package org.moonshot.user.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String socialId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SocialPlatform socialPlatform;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String profileImage;

    private String email;

    private String nickname;

    private String description;

    @Builder
    private User(String socialId, SocialPlatform socialPlatform, String name, String profileImage, String email,
                String nickname, String description) {
        this.socialId = socialId;
        this.socialPlatform = socialPlatform;
        this.name = name;
        this.profileImage = profileImage;
        this.email = email;
        this.nickname = nickname;
        this.description = description;
    }

    @Builder(builderMethodName = "builderWithSignIn")
    public static User of(String socialId, SocialPlatform socialPlatform, String name, String profileImage, String email) {
        return builder()
                .socialId(socialId)
                .socialPlatform(socialPlatform)
                .name(name)
                .profileImage(profileImage)
                .email(email)
                .build();
    }

    public void modifySocialPlatform(SocialPlatform socialPlatform) {
        this.socialPlatform = socialPlatform;
    }

    public void modifyNickname(String nickname) { this.nickname = nickname; }

    public void modifyDescription(String description) { this.description = description; }

}
