package org.moonshot.user.service.social;

import org.moonshot.user.dto.request.SocialLoginRequest;
import org.moonshot.user.dto.response.SocialLoginResponse;

public interface SocialLoginStrategy {

    SocialLoginResponse login(final SocialLoginRequest request);
    boolean support(String provider);

}
