package org.moonshot.server.domain.Task.model;

import jakarta.persistence.*;
import lombok.*;
import org.moonshot.server.domain.keyResult.model.KeyResult;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyResult_id")
    private KeyResult keyResult;

}
