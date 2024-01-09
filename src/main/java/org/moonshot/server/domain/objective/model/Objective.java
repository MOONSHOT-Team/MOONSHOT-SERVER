package org.moonshot.server.domain.objective.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.moonshot.server.domain.user.model.User;
import org.moonshot.server.global.common.model.Period;

@Entity
@Getter
@Builder
@DynamicInsert
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

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isPublic;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isClosed;

    @Column(nullable = false, columnDefinition = "bigint default 0")
    private Long heartCount;

    @Embedded
    private Period period;

    @Builder.Default
    @Column(columnDefinition = "smallint default -1")
    private short order = -1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

}
