package org.moonshot.server.domain.user.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder(builderMethodName = "builderWithSignIn")
    public static User of(String socialId, SocialPlatform socialPlatform, String name, String profileImage, String email) {
        return User.builder()
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
