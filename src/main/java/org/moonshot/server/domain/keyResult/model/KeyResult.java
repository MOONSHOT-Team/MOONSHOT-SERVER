package org.moonshot.server.domain.keyResult.model;

import jakarta.persistence.*;
import lombok.*;
import org.moonshot.server.domain.log.model.Log;
import org.moonshot.server.domain.task.model.Task;
import org.moonshot.server.domain.objective.model.Objective;
import org.moonshot.server.global.common.model.Period;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
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
    private int count;

    @Column(nullable = false)
    private String metric;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private KRState state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "objective_id")
    private Objective objective;

    @OneToMany(mappedBy = "keyResult", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> taskList = new ArrayList<>();

    @OneToMany(mappedBy = "keyResult", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Log> logList = new ArrayList<>();

}
