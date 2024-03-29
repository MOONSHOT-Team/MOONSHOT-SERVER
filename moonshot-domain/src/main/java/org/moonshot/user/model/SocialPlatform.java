package org.moonshot.user.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SocialPlatform {

    KAKAO("kakao"),
    GOOGLE("google");

    private final String value;

}
