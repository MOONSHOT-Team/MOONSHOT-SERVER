package org.moonshot.server.domain.user.dto.response;

import org.moonshot.server.domain.user.model.User;

public record UserInfoResponse(
        String nickname,
        String description
) {
    public static UserInfoResponse of(User user) {
        return new UserInfoResponse(
                user.getNickname(),
                user.getDescription());
    }
}
