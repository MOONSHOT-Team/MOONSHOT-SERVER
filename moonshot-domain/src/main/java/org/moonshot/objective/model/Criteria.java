package org.moonshot.objective.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.moonshot.exception.MoonshotException;
import org.moonshot.response.ErrorType;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Criteria {

    LATEST("최신순"),
    OLDEST("오래된 순"),
    ACCOMPLISH("달성률 순");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Criteria fromValue(String value) {
        for (Criteria criteria : Criteria.values()) {
            if (criteria.getValue().equals(value)) {
                return criteria;
            }
        }
        throw new MoonshotException(ErrorType.INVALID_TYPE);
    }

}
