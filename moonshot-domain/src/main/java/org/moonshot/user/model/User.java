package org.moonshot.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(builderMethodName = "buildWithId")
public class User {

    // TODO 기획 측 약관 확정 이후 수정 필요
    private static final Long USER_RETENTION_PERIOD = 14L;

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
    private String imageUrl;

    private String email;

    private String nickname;

    private String description;

    private LocalDateTime deleteAt;

    @Builder
    private User(String socialId, SocialPlatform socialPlatform, String name, String imageUrl, String email,
                String nickname, String description) {
        this.socialId = socialId;
        this.socialPlatform = socialPlatform;
        this.name = name;
        this.imageUrl = imageUrl;
        this.email = email;
        this.nickname = nickname;
        this.description = description;
    }

    @Builder(builderMethodName = "builderWithSignIn")
    public static User of(String socialId, SocialPlatform socialPlatform, String name, String imageUrl, String email) {
        return builder()
                .socialId(socialId)
                .socialPlatform(socialPlatform)
                .name(name)
                .imageUrl(imageUrl)
                .email(email)
                .build();
    }

    public void modifyNickname(String nickname) { this.nickname = nickname; }

    public void modifyDescription(String description) { this.description = description; }

    public void modifyProfileImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void resetDeleteAt() {
        this.deleteAt = null;
    }

    public void setDeleteAt(){
        this.deleteAt = LocalDateTime.now().plusDays(USER_RETENTION_PERIOD);
    }

}
