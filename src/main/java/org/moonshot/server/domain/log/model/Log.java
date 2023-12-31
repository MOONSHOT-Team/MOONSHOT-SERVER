package org.moonshot.server.domain.log.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.moonshot.server.domain.keyresult.model.KeyResult;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long id;

    @Column(nullable = false)
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LogState state;

    @Builder.Default
    @Column(columnDefinition = "bigint default -1")
    private long prevNum = -1;

    @Column(nullable = false)
    private long currNum;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "key_result_id")
    private KeyResult keyResult;

    public static Log of(LocalDateTime date, LogState state, int prevNum, int currNum, String content, KeyResult keyResult) {
        return Log.builder()
                .date(date)
                .state(state)
                .prevNum(prevNum)
                .currNum(currNum)
                .keyResult(keyResult)
                .build();
    }

}
