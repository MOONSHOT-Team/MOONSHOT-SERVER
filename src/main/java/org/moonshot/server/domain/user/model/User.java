package org.moonshot.server.domain.user.model;

import jakarta.persistence.*;
import lombok.*;
import org.moonshot.server.domain.objective.model.Objective;
import org.moonshot.server.domain.userBadge.model.UserBadge;

import java.util.ArrayList;
import java.util.List;

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

    @Column(nullable = false)
    private String email;

    private String moonshotAccessToken;

    @Column(unique = true)
    private String nickname;

    private String description;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserBadge> badgeList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Objective> objectiveList = new ArrayList<>();

}
