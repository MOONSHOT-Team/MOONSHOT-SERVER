package org.moonshot.server.domain.user.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.moonshot.server.global.constants.RegexConstants;

public record UserInfoRequest(
        @NotNull(message = "닉네임은 필수 입력값입니다.")
        @Pattern(regexp = RegexConstants.nicknameRegex, message = "닉네임은 특수문자, 공백제외 7자까지만 입력 가능합니다.")
        String nickname,
        @Size(min = 1, max = 30, message = "한줄 소개는 30자 이하여야 합니다.")
        String description
) {
    public static UserInfoRequest of(String nickname, String description) {
        return new UserInfoRequest(nickname, description);
    }
}
