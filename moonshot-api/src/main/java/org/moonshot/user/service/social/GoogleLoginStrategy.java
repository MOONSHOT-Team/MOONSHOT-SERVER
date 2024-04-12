package org.moonshot.user.service.social;

import static org.moonshot.user.service.validator.UserValidator.isNewUser;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.moonshot.jwt.JwtTokenProvider;
import org.moonshot.jwt.TokenResponse;
import org.moonshot.openfeign.dto.response.google.GoogleInfoResponse;
import org.moonshot.openfeign.dto.response.google.GoogleTokenResponse;
import org.moonshot.openfeign.google.GoogleApiClient;
import org.moonshot.openfeign.google.GoogleAuthApiClient;
import org.moonshot.user.dto.request.SocialLoginRequest;
import org.moonshot.user.dto.response.SocialLoginResponse;
import org.moonshot.user.model.User;
import org.moonshot.user.repository.UserRepository;
import org.moonshot.user.service.UserSignUpService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GoogleLoginStrategy implements SocialLoginStrategy {

    @Value("${google.client-id}")
    private String googleClientId;
    @Value("${google.client-secret}")
    private String googleClientSecret;
    @Value("${google.redirect-url}")
    private String googleRedirectUrl;

    private final GoogleAuthApiClient googleAuthApiClient;
    private final GoogleApiClient googleApiClient;

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final UserSignUpService userSignUpService;

    @Override
    @Transactional
    public SocialLoginResponse login(SocialLoginRequest request) {
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
            userSignUpService.publishSignUpEvent(newUser);
        } else {
            user = findUser.get();
            user.resetDeleteAt();
        }
        TokenResponse token = new TokenResponse(jwtTokenProvider.generateAccessToken(user.getId()), jwtTokenProvider.generateRefreshToken(user.getId()));
        return SocialLoginResponse.of(user.getId(), user.getName(), token);
    }

    @Override
    public boolean support(String provider) {
        return provider.equals("GOOGLE");
    }

}
