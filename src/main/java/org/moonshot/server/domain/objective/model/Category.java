package org.moonshot.server.domain.objective.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Category {

    ECONOMY("경제"),
    SELFCARE("셀프케어"),
    HEALTH("건강"),
    PRODUCTIVITY("생산성"),
    LIFESTYLE("라이프스타일"),
    GROWTH("성장");

    private final String value;

}
