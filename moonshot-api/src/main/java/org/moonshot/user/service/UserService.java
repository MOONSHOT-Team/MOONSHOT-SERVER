package org.moonshot.user.service;

import static org.moonshot.response.ErrorType.NOT_FOUND_USER;
import static org.moonshot.user.service.validator.UserValidator.hasChange;
import static org.moonshot.user.service.validator.UserValidator.isNewUser;
import static org.moonshot.user.service.validator.UserValidator.validateUserAuthorization;
import static org.moonshot.util.MDCUtil.USER_REQUEST_ORIGIN;
import static org.moonshot.util.MDCUtil.get;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.moonshot.discord.SignUpEvent;
import org.moonshot.exception.NotFoundException;
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
import org.moonshot.user.dto.request.SocialLoginRequest;
import org.moonshot.user.dto.request.UserInfoRequest;
import org.moonshot.user.dto.response.SocialLoginResponse;
import org.moonshot.user.dto.response.UserInfoResponse;
import org.moonshot.user.model.User;
import org.moonshot.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ApplicationEventPublisher eventPublisher;

    private final GoogleAuthApiClient googleAuthApiClient;
    private final GoogleApiClient googleApiClient;
    private final KakaoAuthApiClient kakaoAuthApiClient;
    private final KakaoApiClient kakaoApiClient;
    private final JwtTokenProvider jwtTokenProvider;

    public SocialLoginResponse login(final SocialLoginRequest request) {
        return switch (request.socialPlatform().getValue()) {
            case "google" -> googleLogin(request);
            case "kakao" -> kakaoLogin(request);
            default -> null;
        };
    }

    public SocialLoginResponse googleLogin(final SocialLoginRequest request) {
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
        if (isNewUser(findUser)) {
            User newUser = userRepository.save(User.builderWithSignIn()
                            .socialId(userResponse.sub())
                            .socialPlatform(request.socialPlatform())
                            .name(userResponse.name())
                            .imageUrl(userResponse.picture())
                            .email(userResponse.email())
                            .build());
            user = newUser;
            publishSignUpEvent(newUser);
        } else {
            user = findUser.get();
            user.resetDeleteAt();
        }
        TokenResponse token = new TokenResponse(jwtTokenProvider.generateAccessToken(user.getId()), jwtTokenProvider.generateRefreshToken(user.getId()));
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
        if (isNewUser(findUser)) {
            User newUser = userRepository.save(User.builderWithSignIn()
                            .socialId(userResponse.id())
                            .socialPlatform(request.socialPlatform())
                            .name(userResponse.kakaoAccount().profile().nickname())
                            .imageUrl(userResponse.kakaoAccount().profile().profileImageUrl())
                            .email(null)
                            .build());
            user = newUser;
            publishSignUpEvent(newUser);
        } else {
            user = findUser.get();
            user.resetDeleteAt();
        }
        TokenResponse token = new TokenResponse(jwtTokenProvider.generateAccessToken(user.getId()), jwtTokenProvider.generateRefreshToken(user.getId()));
        return SocialLoginResponse.of(user.getId(), user.getName(), token);
    }

    public TokenResponse reissue(final String refreshToken) {
        String token = refreshToken.substring("Bearer ".length());
        Long userId = jwtTokenProvider.validateRefreshToken(token);
        jwtTokenProvider.deleteRefreshToken(userId);
        return jwtTokenProvider.reissuedToken(userId);
    }

    public void logout(final Long userId) {
        User user =  userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));
        validateUserAuthorization(user.getId(), userId);

        jwtTokenProvider.deleteRefreshToken(userId);
    }

    public void withdrawal(final Long userId) {
        User user =  userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));
        validateUserAuthorization(user.getId(), userId);

        user.setDeleteAt();
    }

    public void modifyProfile(final Long userId, final UserInfoRequest request) {
        User user =  userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));
        validateUserAuthorization(user.getId(), userId);

        if (hasChange(request.nickname())) {
            user.modifyNickname(request.nickname());
        }
        if (hasChange(request.description())) {
            user.modifyDescription(request.description());
        }
    }

    public UserInfoResponse getMyProfile(final Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));
        return UserInfoResponse.of(user);
    }

    public void updateUserProfileImage(final Long userId, final String imageUrl) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));
        user.modifyProfileImage(imageUrl);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void publishSignUpEvent(final User user) {
        eventPublisher.publishEvent(SignUpEvent.of(
                user.getName(),
                user.getEmail() == null ? "" : user.getEmail(),
                user.getSocialPlatform().toString(),
                LocalDateTime.now(),
                user.getImageUrl()
        ));
    }

    public void softDeleteUser(LocalDateTime currentDate) {
        List<User> expiredUserList = userRepository.findIdByDeletedAtBefore(currentDate);
        if(!expiredUserList.isEmpty()) {
            objectiveService.deleteAllObjective(expiredUserList);
            userRepository.deleteAllInBatch(expiredUserList);
        }
    }

}