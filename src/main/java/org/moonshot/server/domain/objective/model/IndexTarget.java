package org.moonshot.server.domain.objective.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum IndexTarget {

    KEY_RESULT("keyResult"),
    TASK("task");

    private final String value;

}
