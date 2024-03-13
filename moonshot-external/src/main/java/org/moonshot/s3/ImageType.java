package org.moonshot.s3;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.moonshot.exception.BadRequestException;
import org.moonshot.response.ErrorType;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ImageType {

    PROFILE("프로필");

    private final String value;

    @JsonCreator
    public static ImageType fromValue(String value) {
        for (ImageType imageType : ImageType.values()) {
            if (imageType.getValue().equals(value)) {
                return imageType;
            }
        }
        throw new BadRequestException(ErrorType.INVALID_TYPE);
    }

}
