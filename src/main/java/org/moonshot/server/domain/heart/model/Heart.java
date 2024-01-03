package org.moonshot.server.domain.heart.model;

import jakarta.persistence.*;
import lombok.*;
import org.moonshot.server.domain.objective.model.Objective;
import org.moonshot.server.domain.user.model.User;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Heart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "heart_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Objective objective;

}
