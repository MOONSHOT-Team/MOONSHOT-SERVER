package org.moonshot.server.domain.userBadge.model;

import jakarta.persistence.*;
import lombok.*;
import org.moonshot.server.domain.badge.model.Badge;
import org.moonshot.server.domain.user.model.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UserBadge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userBadge_id")
    private Long id;

    @Column(nullable = false)
    private LocalDateTime obtainAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_id")
    private Badge badge;

}
