package org.moonshot.server.domain.keyresult.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum KRState {

    WAITING("대기"),
    PROGRESS("진행"),
    DONE("완료"),
    HOLD("보류");

    private final String value;

}
