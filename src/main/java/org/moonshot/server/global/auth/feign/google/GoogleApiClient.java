package org.moonshot.server.global.auth.feign.google;

import org.moonshot.server.domain.user.dto.response.google.GoogleInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "GoogleApiClient", url = "https://www.googleapis.com")
public interface GoogleApiClient {

    @GetMapping("/oauth2/v3/userinfo")
    GoogleInfoResponse googleInfo(
            @RequestHeader("Authorization") String token
    );

}
