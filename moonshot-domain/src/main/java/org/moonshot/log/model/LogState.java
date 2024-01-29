package org.moonshot.log.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum LogState {

    CREATE("KR 생성"),
    UPDATE("KR 수정"),
    RECORD("진척상황 기록");

    private final String value;

}
