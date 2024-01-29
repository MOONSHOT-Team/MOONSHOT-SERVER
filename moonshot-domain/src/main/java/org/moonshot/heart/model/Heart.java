package org.moonshot.heart.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.moonshot.objective.model.Objective;
import org.moonshot.user.model.User;

;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Heart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "heart_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Objective objective;

    @Builder
    private Heart(User user, Objective objective) {
        this.user = user;
        this.objective = objective;
    }
}
