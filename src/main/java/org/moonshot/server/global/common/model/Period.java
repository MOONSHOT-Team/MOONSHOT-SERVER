package org.moonshot.server.global.common.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Period {

    @Column(nullable = false)
    private LocalDate startAt;

    @Column(nullable = false)
    private LocalDate expireAt;

    public static Period of(LocalDate startAt, LocalDate expireAt) {
        return new Period(startAt, expireAt);
    }

}
