package org.moonshot.server.domain.objective.model;

import jakarta.persistence.*;
import lombok.*;
import org.moonshot.server.domain.keyResult.model.KeyResult;
import org.moonshot.server.domain.user.model.User;
import org.moonshot.server.domain.userBadge.model.UserBadge;
import org.moonshot.server.global.common.model.Period;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Objective {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "objective_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean isPublic;

    @Column(nullable = false)
    private boolean isClosed;

    @Column(nullable = false)
    private Long heartCount;

    @Embedded
    private Period period;

    private int order;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "objective", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KeyResult> keyResultList = new ArrayList<>();

}
