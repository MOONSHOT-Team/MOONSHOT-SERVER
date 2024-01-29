package org.moonshot.user.dto.request;


import org.moonshot.user.model.SocialPlatform;

public record SocialLoginRequest(
        SocialPlatform socialPlatform,
        String code
) {
    public static SocialLoginRequest of(SocialPlatform socialPlatform, String code) {
        return new SocialLoginRequest(socialPlatform, code);
    }
}
