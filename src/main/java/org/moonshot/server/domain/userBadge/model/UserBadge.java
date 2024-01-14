package org.moonshot.server.domain.userBadge.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.moonshot.server.domain.badge.model.Badge;
import org.moonshot.server.domain.user.model.User;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserBadge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userBadge_id")
    private Long id;

    @Column(nullable = false)
    private LocalDateTime obtainAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Badge badge;

    @Builder
    public UserBadge(LocalDateTime obtainAt, User user, Badge badge) {
        this.obtainAt = obtainAt;
        this.user = user;
        this.badge = badge;
    }
}
