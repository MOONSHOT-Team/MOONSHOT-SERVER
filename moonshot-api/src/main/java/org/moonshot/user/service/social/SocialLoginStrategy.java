package org.moonshot.user.service.social;

import org.moonshot.user.dto.request.SocialLoginRequest;
import org.moonshot.user.dto.response.SocialLoginResponse;
import org.moonshot.user.model.User;

public interface SocialLoginStrategy {

    SocialLoginResponse login(final SocialLoginRequest request);
    boolean support(String provider);
    void publishSignUpEvent(final User user);

}
