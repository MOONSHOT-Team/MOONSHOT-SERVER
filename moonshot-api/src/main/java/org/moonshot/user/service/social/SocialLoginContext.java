package org.moonshot.user.service.social;

import static org.moonshot.response.ErrorType.NOT_SUPPORTED_LOGIN_PLATFORM;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.moonshot.exception.BadRequestException;
import org.moonshot.user.dto.request.SocialLoginRequest;
import org.moonshot.user.dto.response.SocialLoginResponse;
import org.moonshot.user.model.SocialPlatform;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SocialLoginContext {

    private final GoogleLoginStrategy googleLoginStrategy;
    private final KakaoLoginStrategy kakaoLoginStrategy;
    private final List<SocialLoginStrategy> socialLoginStrategies = new ArrayList<>();

    @PostConstruct
    void initSocialLoginContext() {
        socialLoginStrategies.add(googleLoginStrategy);
        socialLoginStrategies.add(kakaoLoginStrategy);
    }

    public boolean support(SocialPlatform platform) {
        for (SocialLoginStrategy strategy : socialLoginStrategies) {
            if (strategy.support(platform.toString())) {
                return true;
            }
        }
        return false;
    }

    public SocialLoginResponse doLogin(final SocialLoginRequest request) {
        for (SocialLoginStrategy strategy : socialLoginStrategies) {
            if (strategy.support(request.socialPlatform().toString())) {
                return strategy.login(request);
            }
        }
        throw new BadRequestException(NOT_SUPPORTED_LOGIN_PLATFORM);
    }

}
