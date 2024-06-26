package org.moonshot.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.moonshot.jwt.TokenResponse;
import org.moonshot.model.Logging;
import org.moonshot.response.MoonshotResponse;
import org.moonshot.response.SuccessType;
import org.moonshot.user.dto.request.SocialLoginRequest;
import org.moonshot.user.dto.request.UserInfoRequest;
import org.moonshot.user.dto.response.SocialLoginResponse;
import org.moonshot.user.dto.response.UserInfoResponse;
import org.moonshot.user.model.LoginUser;
import org.moonshot.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/user")
public class UserController implements UserApi {

    private final UserService userService;

    @PostMapping("/login")
    @Logging(item = "User", action = "Post")
    public ResponseEntity<MoonshotResponse<SocialLoginResponse>> login(@RequestHeader("Authorization") final String authorization,
                                                                       @RequestBody final SocialLoginRequest socialLoginRequest) {
        return ResponseEntity.ok(MoonshotResponse.success(SuccessType.POST_LOGIN_SUCCESS, userService.login(SocialLoginRequest.of(socialLoginRequest.socialPlatform(), authorization))));
    }

    @PostMapping("/reissue")
    @Logging(item = "User", action = "Post")
    public ResponseEntity<MoonshotResponse<TokenResponse>> reissue(@RequestHeader("Authorization") final String refreshToken) {
        return ResponseEntity.ok(MoonshotResponse.success(SuccessType.POST_REISSUE_SUCCESS, userService.reissue(refreshToken)));
    }

    @PostMapping("/log-out")
    @Logging(item = "User", action = "Post")
    public ResponseEntity<MoonshotResponse<?>> logout(@LoginUser Long userId) {
        userService.logout(userId);
        return ResponseEntity.ok(MoonshotResponse.success(SuccessType.POST_LOGOUT_SUCCESS));
    }

    @DeleteMapping("/withdrawal")
    @Logging(item = "User", action = "Delete")
    public ResponseEntity<?> withdrawal(@LoginUser Long userId) {
        userService.withdrawal(userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/profile")
    @Logging(item = "User", action = "Patch")
    public ResponseEntity<?> modifyProfile(@LoginUser Long userId, @Valid @RequestBody final UserInfoRequest userInfoRequest) {
        userService.modifyProfile(userId, userInfoRequest);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mypage")
    @Logging(item = "User", action = "Get")
    public ResponseEntity<MoonshotResponse<UserInfoResponse>> getMyProfile(@LoginUser Long userId) {
        return ResponseEntity.ok(MoonshotResponse.success(SuccessType.GET_PROFILE_SUCCESS, userService.getMyProfile(userId)));
    }

}