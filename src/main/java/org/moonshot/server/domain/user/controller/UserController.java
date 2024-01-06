package org.moonshot.server.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.moonshot.server.domain.user.dto.request.SocialLoginRequest;
import org.moonshot.server.domain.user.dto.response.SocialLoginResponse;
import org.moonshot.server.domain.user.service.UserService;
import org.moonshot.server.global.auth.jwt.JwtTokenProvider;
import org.moonshot.server.global.auth.jwt.TokenResponse;
import org.moonshot.server.global.common.response.ApiResponse;
import org.moonshot.server.global.common.response.SuccessType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ApiResponse<SocialLoginResponse> login(@RequestHeader("Authorization") String authorization, @RequestBody SocialLoginRequest socialLoginRequest) throws IOException {
        return ApiResponse.success(SuccessType.POST_LOGIN_SUCCESS, userService.login(SocialLoginRequest.of(socialLoginRequest.socialPlatform(), authorization)));
    }

    @PostMapping("/reissue")
    public ApiResponse<TokenResponse> reissue(
            @RequestHeader("Authorization") String refreshToken) {
        return ApiResponse.success(SuccessType.POST_REISSUE_SUCCESS, userService.reissue(refreshToken));
    }

    @PostMapping("/log-out")
    public ApiResponse<?> logout(Principal principal) {
        userService.logout(JwtTokenProvider.getUserFromPrincipal(principal));
        return ApiResponse.success(SuccessType.POST_LOGOUT_SUCCESS);
    }

    @DeleteMapping("/withdrawal")
    public ApiResponse<?> withdrawal(Principal principal) {
        userService.withdrawal(JwtTokenProvider.getUserFromPrincipal(principal));
        return ApiResponse.success(SuccessType.DELETE_USER_SUCCESS);
    }

//    @GetMapping("/login/oauth2/code/kakao")
//    public String kakaoSuccess(@RequestParam String code) {
//        return code;
//    }
//
//    @GetMapping("/login/oauth2/code/google")
//    public String googleSuccess(@RequestParam String code, @RequestParam String scope, @RequestParam String prompt) { return code; }

}