package org.moonshot.user.service;

import static org.moonshot.user.service.validator.UserValidator.validateUserAuthorization;
import static org.moonshot.util.MDCUtil.USER_REQUEST_ORIGIN;
import static org.moonshot.util.MDCUtil.get;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.moonshot.discord.DiscordAppender;
import org.moonshot.exception.global.external.discord.ErrorLogAppenderException;
import org.moonshot.exception.user.UserNotFoundException;
import org.moonshot.jwt.JwtTokenProvider;
import org.moonshot.jwt.TokenResponse;
import org.moonshot.objective.service.ObjectiveService;
import org.moonshot.openfeign.dto.response.google.GoogleInfoResponse;
import org.moonshot.openfeign.dto.response.google.GoogleTokenResponse;
import org.moonshot.openfeign.dto.response.kakao.KakaoTokenResponse;
import org.moonshot.openfeign.dto.response.kakao.KakaoUserResponse;
import org.moonshot.openfeign.google.GoogleApiClient;
import org.moonshot.openfeign.google.GoogleAuthApiClient;
import org.moonshot.openfeign.kakao.KakaoApiClient;
import org.moonshot.openfeign.kakao.KakaoAuthApiClient;
import org.moonshot.security.UserAuthentication;
import org.moonshot.user.dto.request.SocialLoginRequest;
import org.moonshot.user.dto.request.UserInfoRequest;
import org.moonshot.user.dto.response.SocialLoginResponse;
import org.moonshot.user.dto.response.UserInfoResponse;
import org.moonshot.user.model.User;
import org.moonshot.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
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

    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;

    private final UserRepository userRepository;
    private final ObjectiveService objectiveService;

    private final GoogleAuthApiClient googleAuthApiClient;
    private final GoogleApiClient googleApiClient;
    private final KakaoAuthApiClient kakaoAuthApiClient;
    private final KakaoApiClient kakaoApiClient;
    private final JwtTokenProvider jwtTokenProvider;

    public SocialLoginResponse login(final SocialLoginRequest request) throws IOException {
        return switch (request.socialPlatform().getValue()) {
            case "google" -> googleLogin(request);
            case "kakao" -> kakaoLogin(request);
            default -> null;
        };
    }

    public SocialLoginResponse googleLogin(final SocialLoginRequest request) throws IOException {
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
            sendDiscordAlert(newUser);
        } else {
            user = findUser.get();
            user.resetDeleteAt();
        }
        UserAuthentication userAuthentication = new UserAuthentication(user.getId(), null, null);
        TokenResponse token = new TokenResponse(jwtTokenProvider.generateAccessToken(userAuthentication), jwtTokenProvider.generateRefreshToken(userAuthentication));
        return SocialLoginResponse.of(user.getId(), user.getName(), token);
    }

    public SocialLoginResponse kakaoLogin(final SocialLoginRequest request) {
        KakaoTokenResponse tokenResponse = kakaoAuthApiClient.getOAuth2AccessToken(
                "authorization_code",
                kakaoClientId,
                (String)get(USER_REQUEST_ORIGIN) + kakaoRedirectUri,
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
            sendDiscordAlert(newUser);
        } else {
            user = findUser.get();
            user.resetDeleteAt();
        }
        UserAuthentication userAuthentication = new UserAuthentication(user.getId(), null, null);
        TokenResponse token = new TokenResponse(jwtTokenProvider.generateAccessToken(userAuthentication), jwtTokenProvider.generateRefreshToken(userAuthentication));
        return SocialLoginResponse.of(user.getId(), user.getName(), token);
    }

    public TokenResponse reissue(final String refreshToken) {
        String token = refreshToken.substring("Bearer ".length());
        Long userId = jwtTokenProvider.validateRefreshToken(token);
        jwtTokenProvider.deleteRefreshToken(userId);
        UserAuthentication userAuthentication = new UserAuthentication(userId, null, null);
        return jwtTokenProvider.reissuedToken(userAuthentication);
    }

    public void logout(final Long userId) {
        User user =  userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        validateUserAuthorization(user.getId(), userId);

        jwtTokenProvider.deleteRefreshToken(userId);
    }

    public void withdrawal(final Long userId) {
        User user =  userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        validateUserAuthorization(user.getId(), userId);

        user.setDeleteAt();
    }

    public void modifyProfile(final Long userId, final UserInfoRequest request) {
        User user =  userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        validateUserAuthorization(user.getId(), userId);

        if (request.nickname() != null) {
            user.modifyNickname(request.nickname());
        }
        if (request.description() != null) {
            user.modifyDescription(request.description());
        }
    }

    public UserInfoResponse getMyProfile(final Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        return UserInfoResponse.of(user);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendDiscordAlert(final User user) {
        try {
            DiscordAppender discordAppender = new DiscordAppender();
            discordAppender.signInAppend(user.getName(), user.getEmail() == null ? "" : user.getEmail(), user.getSocialPlatform().getValue(), LocalDateTime.now(), user.getProfileImage());
        } catch (ErrorLogAppenderException e) {
            log.error("{}", e.getErrorType().getMessage());
        }
    }

    public void softDeleteUser(LocalDateTime currentDate) {
        List<User> expiredUserList = userRepository.findIdByDeletedAtBefore(currentDate);
        if(!expiredUserList.isEmpty()) {
            objectiveService.deleteAllObjective(expiredUserList);
            userRepository.deleteAllInBatch(expiredUserList);
        }
    }

}