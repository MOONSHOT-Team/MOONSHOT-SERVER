package org.moonshot.server.domain.keyresult.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicInsert;
import org.moonshot.server.domain.objective.model.Objective;
import org.moonshot.server.domain.task.model.Task;
import org.moonshot.server.global.common.model.Period;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private Long target;

    @Column(nullable = false)
    private Integer idx;

    @Column(nullable = false)
    private String metric;

    private String descriptionBefore;

    private String descriptionAfter;

    private short progress = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private KRState state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "objective_id")
    private Objective objective;

    @JsonIgnore
    @BatchSize(size = 200)
    @OneToMany(mappedBy = "keyResult", fetch = FetchType.LAZY)
    List<Task> taskList = new ArrayList<>();

    public void incrementIdx() {
        ++this.idx;
    }

    public void decreaseIdx() {
        --this.idx;
    }

    public void modifyTitle(String title) {
        this.title = title;
    }

    public void modifyPeriod(Period period) {
        this.period = period;
    }

    public void modifyIdx(Integer idx) {
        this.idx = idx;
    }

    public void modifyTarget(Long target) {
        this.target = target;
    }

    public void modifyState(KRState state) {
        this.state = state;
    }

    public void modifyProgress(short progress) {
        this.progress = progress;
    }

    @Builder
    private KeyResult(String title, Period period, Long target, Integer idx, String metric, String descriptionBefore,
                     String descriptionAfter, KRState state, Objective objective) {
        this.title = title;
        this.period = period;
        this.target = target;
        this.idx = idx;
        this.metric = metric;
        this.descriptionBefore = descriptionBefore;
        this.descriptionAfter = descriptionAfter;
        this.progress = 0;
        this.state = KRState.PROGRESS;
        this.objective = objective;
        this.taskList = new ArrayList<>();
    }
}
