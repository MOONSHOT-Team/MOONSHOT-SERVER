package org.moonshot.server.domain.task.model;

import jakarta.persistence.*;
import lombok.*;
import org.moonshot.server.domain.keyresult.model.KeyResult;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "key_result_id")
    private KeyResult keyResult;

    public void incrementIdx() {
        ++this.idx;
    }

    public void modifyIdx(Integer idx) {
        this.idx = idx;
    }

}