package org.moonshot.objective.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum IndexTarget {

    OBJECTIVE("objective"),
    KEY_RESULT("keyResult"),
    TASK("task");

    private final String value;

}
