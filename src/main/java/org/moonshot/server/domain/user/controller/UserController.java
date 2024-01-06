package org.moonshot.server.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.moonshot.server.domain.user.dto.request.SocialLoginRequest;
import org.moonshot.server.domain.user.dto.response.SocialLoginResponse;
import org.moonshot.server.domain.user.model.CurrentUser;
import org.moonshot.server.domain.user.service.UserService;
import org.moonshot.server.global.auth.jwt.TokenResponse;
import org.moonshot.server.global.common.response.ApiResponse;
import org.moonshot.server.global.common.response.SuccessType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ApiResponse<SocialLoginResponse> login(@RequestHeader("Authorization") String authorization, @RequestBody SocialLoginRequest socialLoginRequest) throws IOException {
        return ApiResponse.success(SuccessType.POST_LOGIN_SUCCESS, userService.login(SocialLoginRequest.of(socialLoginRequest.socialPlatform(), authorization)));
    }

    @PostMapping("/reissue")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<TokenResponse> reissue(
            @RequestHeader("Authorization") String refreshToken) throws IOException {
        return ApiResponse.success(SuccessType.POST_REISSUE_SUCCESS, userService.reissue(refreshToken));
    }

    @PostMapping("/log-out")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> logout(@CurrentUser Long userId) {
        userService.logout(userId);
        return ApiResponse.success(SuccessType.POST_LOGOUT_SUCCESS);
    }

    @GetMapping("/login/oauth2/code/kakao")
    public String kakaoSuccess(@RequestParam String code) {
        return code;
    }

    @GetMapping("/login/oauth2/code/google")
    public String googleSuccess(@RequestParam String code, @RequestParam String scope, @RequestParam String prompt) { return code; }
}