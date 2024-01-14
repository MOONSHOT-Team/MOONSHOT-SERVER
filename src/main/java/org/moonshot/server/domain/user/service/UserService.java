package org.moonshot.server.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.moonshot.server.domain.user.dto.request.SocialLoginRequest;
import org.moonshot.server.domain.user.dto.request.UserInfoRequest;
import org.moonshot.server.domain.user.dto.response.SocialLoginResponse;
import org.moonshot.server.domain.user.dto.response.UserInfoResponse;
import org.moonshot.server.domain.user.dto.response.google.GoogleInfoResponse;
import org.moonshot.server.domain.user.dto.response.google.GoogleTokenResponse;
import org.moonshot.server.domain.user.dto.response.kakao.KakaoTokenResponse;
import org.moonshot.server.domain.user.dto.response.kakao.KakaoUserResponse;
import org.moonshot.server.domain.user.exception.UserNotFoundException;
import org.moonshot.server.domain.user.model.SocialPlatform;
import org.moonshot.server.domain.user.model.User;
import org.moonshot.server.domain.user.repository.UserRepository;
import org.moonshot.server.global.auth.exception.AccessDeniedException;
import org.moonshot.server.global.auth.feign.google.GoogleApiClient;
import org.moonshot.server.global.auth.feign.google.GoogleAuthApiClient;
import org.moonshot.server.global.auth.feign.kakao.KakaoApiClient;
import org.moonshot.server.global.auth.feign.kakao.KakaoAuthApiClient;
import org.moonshot.server.global.auth.jwt.JwtTokenProvider;
import org.moonshot.server.global.auth.jwt.TokenResponse;
import org.moonshot.server.global.auth.security.UserAuthentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    @Value("${google.client-id}")
    private String googleClientId;

    @Value("${google.client-secret}")
    private String googleClientSecret;

    @Value("${google.redirect-url}")
    private String googleRedirectUrl;

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.redirect-url}")
    private String kakaoRedirectUrl;

    private final UserRepository userRepository;
    private final GoogleAuthApiClient googleAuthApiClient;
    private final GoogleApiClient googleApiClient;
    private final KakaoAuthApiClient kakaoAuthApiClient;
    private final KakaoApiClient kakaoApiClient;
    private final JwtTokenProvider jwtTokenProvider;

    public SocialLoginResponse login(SocialLoginRequest request) throws IOException {
        switch (request.socialPlatform().getValue()){
            case "google":
                return googleLogin(request);
            case "kakao":
                return kakaoLogin(request);
        }
        return null;
    }

    public SocialLoginResponse googleLogin(SocialLoginRequest request) throws IOException {
        GoogleTokenResponse tokenResponse = googleAuthApiClient.googleAuth(
                request.code(),
                googleClientId,
                googleClientSecret,
                googleRedirectUrl,
                "authorization_code"
        );
        GoogleInfoResponse userResponse = googleApiClient.googleInfo("Bearer " + tokenResponse.accessToken());
        Optional<User> findUser = userRepository.findUserBySocialId(userResponse.sub());
        User user;
        if (findUser.isEmpty()) {
            User newUser = userRepository.save(User.builderWithSignIn()
                            .socialId(userResponse.sub())
                            .socialPlatform(request.socialPlatform())
                            .name(userResponse.name())
                            .profileImage(userResponse.picture())
                            .email(userResponse.email())
                            .build());
            user = newUser;
        } else {
            user = findUser.get();
            if (user.getSocialPlatform().equals(SocialPlatform.WITHDRAWAL)) {
                user.modifySocialPlatform(SocialPlatform.GOOGLE);
            }
        }
        UserAuthentication userAuthentication = new UserAuthentication(user.getId(), null, null);
        TokenResponse token = new TokenResponse(jwtTokenProvider.generateAccessToken(userAuthentication), jwtTokenProvider.generateRefreshToken(userAuthentication));
        return SocialLoginResponse.of(user.getId(), user.getName(), token);
    }

    public SocialLoginResponse kakaoLogin(SocialLoginRequest request) throws IOException {
        KakaoTokenResponse tokenResponse = kakaoAuthApiClient.getOAuth2AccessToken(
                "authorization_code",
                kakaoClientId,
                kakaoRedirectUrl,
                request.code()
        );
        KakaoUserResponse userResponse = kakaoApiClient.getUserInformation(
                "Bearer " + tokenResponse.accessToken());
        Optional<User> findUser = userRepository.findUserBySocialId(userResponse.id());
        User user;
        if (findUser.isEmpty()) {
            User newUser = userRepository.save(User.builderWithSignIn()
                            .socialId(userResponse.id())
                            .socialPlatform(request.socialPlatform())
                            .name(userResponse.kakaoAccount().profile().nickname())
                            .profileImage(userResponse.kakaoAccount().profile().profileImageUrl())
                            .email(null)
                            .build());
            user = newUser;
        } else {
            user = findUser.get();
            if (user.getSocialPlatform().equals(SocialPlatform.WITHDRAWAL)) {
                user.modifySocialPlatform(SocialPlatform.KAKAO);
            }
        }
        UserAuthentication userAuthentication = new UserAuthentication(user.getId(), null, null);
        TokenResponse token = new TokenResponse(jwtTokenProvider.generateAccessToken(userAuthentication), jwtTokenProvider.generateRefreshToken(userAuthentication));
        return SocialLoginResponse.of(user.getId(), user.getName(), token);
    }

    public TokenResponse reissue(String refreshToken) {
        String token = refreshToken.substring("Bearer ".length());
        Long userId = jwtTokenProvider.validateRefreshToken(token);
        jwtTokenProvider.deleteRefreshToken(userId);
        UserAuthentication userAuthentication = new UserAuthentication(userId, null, null);
        return jwtTokenProvider.reissuedToken(userAuthentication);
    }

    public void logout(Long userId) {
        jwtTokenProvider.deleteRefreshToken(userId);
    }

    public void withdrawal(Long userId) {
        User user =  userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        user.modifySocialPlatform(SocialPlatform.WITHDRAWAL);
    }

    public void modifyProfile(Long userId, UserInfoRequest request) {
        User user =  userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        if (request.nickname() != null) {
            user.modifyNickname(request.nickname());
        }
        if (request.description() != null) {
            user.modifyDescription(request.description());
        }
    }

    public void validateUserAuthorization(User user, Long userId) {
        if (!user.getId().equals(userId)) {
            throw new AccessDeniedException();
        }
    }

    public UserInfoResponse getMyProfile(Long userId) {
        User user =  userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        return UserInfoResponse.of(user);
    }

}