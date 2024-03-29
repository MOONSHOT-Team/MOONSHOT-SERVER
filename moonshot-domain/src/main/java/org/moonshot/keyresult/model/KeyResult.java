package org.moonshot.keyresult.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicInsert;
import org.moonshot.common.model.Period;
import org.moonshot.objective.model.Objective;
import org.moonshot.task.model.Task;

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
    private KeyResult(String title, Period period, Long target, Integer idx, String metric, KRState state, Objective objective) {
        this.title = title;
        this.period = period;
        this.target = target;
        this.idx = idx;
        this.metric = metric;
        this.progress = 0;
        this.state = KRState.PROGRESS;
        this.objective = objective;
        this.taskList = new ArrayList<>();
    }
}
