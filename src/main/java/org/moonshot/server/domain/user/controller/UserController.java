package org.moonshot.server.domain.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.moonshot.server.domain.user.dto.request.UserInfoRequest;
import org.moonshot.server.domain.user.dto.response.UserInfoResponse;
import org.moonshot.server.domain.user.dto.request.SocialLoginRequest;
import org.moonshot.server.domain.user.dto.response.SocialLoginResponse;
import org.moonshot.server.domain.user.service.UserService;
import org.moonshot.server.global.auth.jwt.JwtTokenProvider;
import org.moonshot.server.global.auth.jwt.TokenResponse;
import org.moonshot.server.global.common.response.MoonshotResponse;
import org.moonshot.server.global.common.response.SuccessType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/user")
public class UserController implements UserApi {
    @Value("${google.client-id}")
    private String googleClientId;

    @Value("${google.client-secret}")
    private String googleClientSecret;

    @Value("${google.redirect-url}")
    private String googleRedirectUrl;

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<MoonshotResponse<SocialLoginResponse>> login(@RequestHeader("Authorization") final String authorization,
                                                                       @RequestBody final SocialLoginRequest socialLoginRequest) throws IOException {
        return ResponseEntity.ok(MoonshotResponse.success(SuccessType.POST_LOGIN_SUCCESS, userService.login(SocialLoginRequest.of(socialLoginRequest.socialPlatform(), authorization))));
    }

    @PostMapping("/reissue")
    public ResponseEntity<MoonshotResponse<TokenResponse>> reissue(@RequestHeader("Authorization") final String refreshToken) {
        return ResponseEntity.ok(MoonshotResponse.success(SuccessType.POST_REISSUE_SUCCESS, userService.reissue(refreshToken)));
    }

    @PostMapping("/log-out")
    public ResponseEntity<MoonshotResponse<?>> logout(final Principal principal) {
        userService.logout(JwtTokenProvider.getUserIdFromPrincipal(principal));
        return ResponseEntity.ok(MoonshotResponse.success(SuccessType.POST_LOGOUT_SUCCESS));
    }

    @DeleteMapping("/withdrawal")
    public ResponseEntity<?> withdrawal(final Principal principal) {
        userService.withdrawal(JwtTokenProvider.getUserIdFromPrincipal(principal));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/profile")
    public ResponseEntity<?> modifyProfile(final Principal principal, @Valid  @RequestBody final UserInfoRequest userInfoRequest) {
        userService.modifyProfile(JwtTokenProvider.getUserIdFromPrincipal(principal), userInfoRequest);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mypage")
    public ResponseEntity<MoonshotResponse<UserInfoResponse>> getMyProfile(final Principal principal) {
        return ResponseEntity.ok(MoonshotResponse.success(SuccessType.GET_PROFILE_SUCCESS, userService.getMyProfile(JwtTokenProvider.getUserIdFromPrincipal(principal))));
    }

    @GetMapping("/googleLogin")
    public String authTest(final HttpServletRequest request, final HttpServletResponse response) {
        String redirectURL = "https://accounts.google.com/o/oauth2/v2/auth?client_id=" + googleClientId
                + "&redirect_uri=" + googleRedirectUrl + "&response_type=code&scope=email profile";
        try {
            response.sendRedirect(redirectURL);
        } catch (Exception e) {
            log.info("authTest = {}", e);
        }

        return "SUCCESS";
    }

//    @GetMapping("/login/oauth2/code/kakao")
//    public String kakaoSuccess(@RequestParam String code) {
//        return code;
//    }
//
//    @GetMapping("/login/oauth2/code/google")
//    public String googleSuccess(@RequestParam String code, @RequestParam String scope, @RequestParam String prompt) { return code; }

}