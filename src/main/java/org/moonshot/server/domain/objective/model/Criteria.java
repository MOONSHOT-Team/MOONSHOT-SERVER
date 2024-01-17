package org.moonshot.server.domain.objective.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Criteria {

    LATEST("최신순"),
    OLDEST("오래된 순"),
    ACCOMPLISH("달성률");

    private final String value;

}
