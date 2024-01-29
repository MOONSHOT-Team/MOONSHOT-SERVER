package org.moonshot.openfeign.google;

import org.moonshot.openfeign.dto.response.google.GoogleTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "GoogleAuthApiClient", url = "https://oauth2.googleapis.com/token")
public interface GoogleAuthApiClient {

    @PostMapping
    GoogleTokenResponse googleAuth(
            @RequestParam(name = "code") String code,
            @RequestParam(name = "clientId") String clientId,
            @RequestParam(name = "clientSecret") String clientSecret,
            @RequestParam(name = "redirectUri") String redirectUri,
            @RequestParam(name = "grantType") String grantType);

}
