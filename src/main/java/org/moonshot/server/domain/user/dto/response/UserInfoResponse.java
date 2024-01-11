package org.moonshot.server.domain.user.dto.response;

import org.moonshot.server.domain.user.model.SocialPlatform;
import org.moonshot.server.domain.user.model.User;

public record UserInfoResponse(
        String socialPlatform,
        String profileImgUrl,
        String nickname,
        String description
) {
    public static UserInfoResponse of(User user) {
        return new UserInfoResponse(
                user.getSocialPlatform().getValue(),
                user.getProfileImage(),
                user.getNickname(),
                user.getDescription());
    }
}
