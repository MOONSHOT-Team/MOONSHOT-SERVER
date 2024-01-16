package org.moonshot.server.domain.objective.model;

import jakarta.persistence.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.moonshot.server.domain.keyresult.model.KeyResult;
import org.moonshot.server.domain.user.model.User;
import org.moonshot.server.global.common.model.Period;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Objective {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "objective_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = false)
    private String content;

    private short progress;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isPublic;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isClosed;

    @Column(nullable = false)
    private Long heartCount;

    @Embedded
    private Period period;

    private int idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @BatchSize(size = 200)
    @OneToMany(mappedBy = "objective", fetch = FetchType.LAZY)
    List<KeyResult> keyResultList;

    @Builder
    private Objective(String title, Category category, String content, Period period, User user) {
        this.title = title;
        this.category = category;
        this.content = content;
        this.progress = 0;
        this.isPublic = false;
        this.isClosed = false;
        this.heartCount = 0L;
        this.period = period;
        this.idx = 0;
        this.user = user;
        this.keyResultList = new ArrayList<>();
    }

    public String getDateString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.MM.dd");
        return this.period.getStartAt().format(formatter) + " - " + this.period.getExpireAt().format(formatter);
    }

    public void  modifyClosed(boolean isClosed) {
        this.isClosed = isClosed;
    }

    public void modifyPeriod(Period period) {
        this.period = period;
    }

    public void modifyProgress(short progress) {
        this.progress = progress;
    }

    public void modifyIdx(Integer idx) {
        this.idx = idx;
    }

}
