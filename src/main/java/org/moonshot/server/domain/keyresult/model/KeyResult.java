package org.moonshot.server.domain.keyresult.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.moonshot.server.domain.objective.model.Objective;
import org.moonshot.server.global.common.model.Period;

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
    private Integer target;

    @Column(nullable = false)
    private Short idx;

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

    public void incrementIdx() {
        ++this.idx;
    }

    public void modifyTitle(String title) {
        this.title = title;
    }

    public void modifyPeriod(Period period) {
        this.period = period;
    }

    public void modifyIdx(Short idx) {
        this.idx = idx;
    }

    public void modifyTarget(Integer target) {
        this.target = target;
    }

    public void modifyState(KRState state) {
        this.state = state;
    }

}
