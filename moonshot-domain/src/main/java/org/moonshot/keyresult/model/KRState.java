package org.moonshot.keyresult.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.moonshot.exception.BadRequestException;
import org.moonshot.response.ErrorType;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum KRState {

    WAITING("대기"),
    PROGRESS("진행"),
    DONE("완료"),
    HOLD("보류");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static KRState fromValue(String value) {
        for (KRState krState : KRState.values()) {
            if (krState.getValue().equals(value)) {
                return krState;
            }
        }
        throw new BadRequestException(ErrorType.INVALID_TYPE);
    }

}
