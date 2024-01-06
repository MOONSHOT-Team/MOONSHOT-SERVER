package org.moonshot.server.domain.user.dto.request;

import org.moonshot.server.domain.user.model.SocialPlatform;

public record SocialLoginRequest(
        SocialPlatform socialPlatform,
        String code
) {
    public static SocialLoginRequest of(SocialPlatform socialPlatform, String code) {
        return new SocialLoginRequest(socialPlatform, code);
    }
}
