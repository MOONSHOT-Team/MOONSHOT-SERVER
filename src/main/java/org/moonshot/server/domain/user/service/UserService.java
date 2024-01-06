package org.moonshot.server.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.moonshot.server.domain.user.dto.request.SocialLoginRequest;
import org.moonshot.server.domain.user.dto.response.SocialLoginResponse;
import org.moonshot.server.domain.user.dto.response.google.GoogleInfoResponse;
import org.moonshot.server.domain.user.dto.response.google.GoogleTokenResponse;
import org.moonshot.server.domain.user.dto.response.kakao.KakaoTokenResponse;
import org.moonshot.server.domain.user.dto.response.kakao.KakaoUserResponse;
import org.moonshot.server.domain.user.model.SocialPlatform;
import org.moonshot.server.domain.user.model.User;
import org.moonshot.server.domain.user.repository.UserRepository;
import org.moonshot.server.global.auth.feign.google.GoogleApiClient;
import org.moonshot.server.global.auth.feign.google.GoogleAuthApiClient;
import org.moonshot.server.global.auth.feign.kakao.KakaoApiClient;
import org.moonshot.server.global.auth.feign.kakao.KakaoAuthApiClient;
import org.moonshot.server.global.auth.jwt.JwtTokenProvider;
import org.moonshot.server.global.auth.security.UserAuthentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
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
        System.out.println(request.socialPlatform().getValue());
        switch (request.socialPlatform().getValue()){
            case "google":
                return gooleLogin(request);
            case "kakao":
                return kakaoLogin(request);
        }
        return null;
    }

    public SocialLoginResponse gooleLogin(SocialLoginRequest request) throws IOException {
        GoogleTokenResponse tokenResponse = googleAuthApiClient.googleAuth(
                request.code(),
                googleClientId,
                googleClientSecret,
                googleRedirectUrl,
                "authorization_code"
        );

        GoogleInfoResponse userResponse = googleApiClient.googleInfo("Bearer " + tokenResponse.accessToken());

        UserAuthentication userAuthentication = new UserAuthentication(userResponse.sub(), null, null);
        String  moonshotAccessToken = jwtTokenProvider.generateAccessToken(userAuthentication);

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
        }

        return SocialLoginResponse.of(user.getId(), user.getName());
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
        UserAuthentication userAuthentication = new UserAuthentication(userResponse.id(), null, null);
        String  moonshotAccessToken = jwtTokenProvider.generateAccessToken(userAuthentication);

        Optional<User> findUser = userRepository.findUserBySocialId(userResponse.id());
        User user;
        if (findUser.isEmpty()) {
            User newUser = userRepository.save(User.builderWithSignIn()
                            .socialId(userResponse.id())
                            .socialPlatform(request.socialPlatform())
                            .name(userResponse.kakaoAccount().profile().nickname())
                            .profileImage(userResponse.kakaoAccount().profile().profileImageUrl())
                            .email("")
                            .build());

            user = newUser;
        } else {
            user = findUser.get();
        }

        return SocialLoginResponse.of(user.getId(), user.getName());
    }

}