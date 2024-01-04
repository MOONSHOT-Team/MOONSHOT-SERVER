package org.moonshot.server.domain.keyresult.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.moonshot.server.domain.log.model.Log;
import org.moonshot.server.domain.task.model.Task;
import org.moonshot.server.domain.objective.model.Objective;
import org.moonshot.server.global.common.model.Period;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class KeyResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "key_result_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Embedded
    private Period period;

    @Column(nullable = false)
    private int target;

    @Column(nullable = false)
    private String metric;

    @Column(nullable = false)
    private String descriptionBefore;

    @Column(nullable = false)
    private String descriptionAfter;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "enum('DONE','HOLD','PROGRESS','WAITING') default 'PROGRESS'")
    private KRState state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "objective_id")
    private Objective objective;

}
