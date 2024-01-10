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
    private Long target;

    @Column(nullable = false)
    private Integer idx;

    @Column(nullable = false)
    private String metric;

    private String descriptionBefore;

    private String descriptionAfter;

    @Builder.Default
    private short progress = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "enum('DONE','HOLD','PROGRESS','WAITING') default 'PROGRESS'")
    private KRState state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "objective_id")
    private Objective objective;

    @JsonIgnore
    @BatchSize(size = 10)
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

}
