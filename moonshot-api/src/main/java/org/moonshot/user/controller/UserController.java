package org.moonshot.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.moonshot.jwt.JwtTokenProvider;
import org.moonshot.jwt.TokenResponse;
import org.moonshot.model.Logging;
import org.moonshot.response.MoonshotResponse;
import org.moonshot.response.SuccessType;
import org.moonshot.user.dto.request.SocialLoginRequest;
import org.moonshot.user.dto.request.UserInfoRequest;
import org.moonshot.user.dto.response.SocialLoginResponse;
import org.moonshot.user.dto.response.UserInfoResponse;
import org.moonshot.user.service.UserService;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${google.client-id}")
    private String googleClientId;

    @Value("${google.client-secret}")
    private String googleClientSecret;

    @Value("${google.redirect-url}")
    private String googleRedirectUrl;

    private final UserService userService;

    @PostMapping("/login")
    @Logging(item = "User", action = "Post")
    public ResponseEntity<MoonshotResponse<SocialLoginResponse>> login(@RequestHeader("Authorization") final String authorization,
                                                                       @RequestBody final SocialLoginRequest socialLoginRequest) throws IOException {
        return ResponseEntity.ok(MoonshotResponse.success(SuccessType.POST_LOGIN_SUCCESS, userService.login(SocialLoginRequest.of(socialLoginRequest.socialPlatform(), authorization))));
    }

    @PostMapping("/reissue")
    @Logging(item = "User", action = "Post")
    public ResponseEntity<MoonshotResponse<TokenResponse>> reissue(@RequestHeader("Authorization") final String refreshToken) {
        return ResponseEntity.ok(MoonshotResponse.success(SuccessType.POST_REISSUE_SUCCESS, userService.reissue(refreshToken)));
    }

    @PostMapping("/log-out")
    @Logging(item = "User", action = "Post")
    public ResponseEntity<MoonshotResponse<?>> logout(final Principal principal) {
        userService.logout(JwtTokenProvider.getUserIdFromPrincipal(principal));
        return ResponseEntity.ok(MoonshotResponse.success(SuccessType.POST_LOGOUT_SUCCESS));
    }

    @DeleteMapping("/withdrawal")
    @Logging(item = "User", action = "Delete")
    public ResponseEntity<?> withdrawal(final Principal principal) {
        userService.withdrawal(JwtTokenProvider.getUserIdFromPrincipal(principal));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/profile")
    @Logging(item = "User", action = "Patch")
    public ResponseEntity<?> modifyProfile(final Principal principal, @Valid @RequestBody final UserInfoRequest userInfoRequest) {
        userService.modifyProfile(JwtTokenProvider.getUserIdFromPrincipal(principal), userInfoRequest);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mypage")
    @Logging(item = "User", action = "Get")
    public ResponseEntity<MoonshotResponse<UserInfoResponse>> getMyProfile(final Principal principal) {
        return ResponseEntity.ok(MoonshotResponse.success(SuccessType.GET_PROFILE_SUCCESS, userService.getMyProfile(JwtTokenProvider.getUserIdFromPrincipal(principal))));
    }

    @GetMapping("/googleLogin")
    @Logging(item = "User", action = "Get")
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