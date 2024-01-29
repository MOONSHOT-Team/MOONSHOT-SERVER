package org.moonshot.user.dto.response;


import org.moonshot.user.model.User;

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
