package org.moonshot.server.domain.task.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.moonshot.server.domain.keyresult.model.KeyResult;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer idx;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "key_result_id")
    private KeyResult keyResult;

    public void incrementIdx() {
        ++this.idx;
    }

    public void modifyIdx(Integer idx) {
        this.idx = idx;
    }

    @Builder
    private Task(String title, Integer idx, KeyResult keyResult) {
        this.title = title;
        this.idx = idx;
        this.keyResult = keyResult;
    }
}