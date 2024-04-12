package org.moonshot.user.service.social;

import static org.moonshot.user.service.validator.UserValidator.isNewUser;
import static org.moonshot.util.MDCUtil.USER_REQUEST_ORIGIN;
import static org.moonshot.util.MDCUtil.get;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.moonshot.discord.SignUpEvent;
import org.moonshot.jwt.JwtTokenProvider;
import org.moonshot.jwt.TokenResponse;
import org.moonshot.openfeign.dto.response.kakao.KakaoTokenResponse;
import org.moonshot.openfeign.dto.response.kakao.KakaoUserResponse;
import org.moonshot.openfeign.kakao.KakaoApiClient;
import org.moonshot.openfeign.kakao.KakaoAuthApiClient;
import org.moonshot.user.dto.request.SocialLoginRequest;
import org.moonshot.user.dto.response.SocialLoginResponse;
import org.moonshot.user.model.User;
import org.moonshot.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KakaoLoginStrategy implements SocialLoginStrategy {

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;

    private final KakaoAuthApiClient kakaoAuthApiClient;
    private final KakaoApiClient kakaoApiClient;

    private final ApplicationEventPublisher eventPublisher;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public SocialLoginResponse login(SocialLoginRequest request) {
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

    @Override
    public boolean support(String provider) {
        return provider.equals("KAKAO");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void publishSignUpEvent(User user) {
        eventPublisher.publishEvent(SignUpEvent.of(
                user.getName(),
                user.getEmail() == null ? "" : user.getEmail(),
                user.getSocialPlatform().toString(),
                LocalDateTime.now(),
                user.getImageUrl()
        ));
    }

}
