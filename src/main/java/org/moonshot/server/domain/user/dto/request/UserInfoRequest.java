package org.moonshot.server.domain.user.dto.request;

public record UserInfoRequest(
        String nickname,
        String description
) {
    public static UserInfoRequest of(String nickname, String description) {
        return new UserInfoRequest(nickname, description);
    }
}
