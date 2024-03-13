package org.moonshot.s3;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ImageType {

    PROFILE("프로필");

    private final String value;

}
