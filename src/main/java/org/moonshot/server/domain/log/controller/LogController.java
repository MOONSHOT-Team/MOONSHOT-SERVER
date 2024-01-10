package org.moonshot.server.domain.log.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.moonshot.server.domain.log.dto.request.LogCreateRequestDto;
import org.moonshot.server.domain.log.dto.response.AchieveResponseDto;
import org.moonshot.server.domain.log.service.LogService;
import org.moonshot.server.global.auth.jwt.JwtTokenProvider;
import org.moonshot.server.global.common.response.ApiResponse;
import org.moonshot.server.global.common.response.SuccessType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/log")
public class LogController {

    private final LogService logService;

    @PostMapping
    public ApiResponse<Optional<AchieveResponseDto>> create(Principal principal, @RequestBody @Valid LogCreateRequestDto logCreateRequestDto) {
        return ApiResponse.success(SuccessType.POST_LOG_SUCCESS, logService.createRecordLog(JwtTokenProvider.getUserIdFromPrincipal(principal), logCreateRequestDto));
    }

}
