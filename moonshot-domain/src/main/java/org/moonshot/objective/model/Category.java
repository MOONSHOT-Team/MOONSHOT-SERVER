package org.moonshot.objective.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.moonshot.exception.BadRequestException;
import org.moonshot.response.ErrorType;

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

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Category fromValue(String value) {
        for (Category category : Category.values()) {
            if (category.getValue().equals(value)) {
                return category;
            }
        }
        throw new BadRequestException(ErrorType.INVALID_TYPE);
    }

}
