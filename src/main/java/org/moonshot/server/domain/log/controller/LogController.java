package org.moonshot.server.domain.log.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.moonshot.server.domain.log.dto.request.LogCreateRequestDto;
import org.moonshot.server.domain.log.dto.response.AchieveResponseDto;
import org.moonshot.server.domain.log.service.LogService;
import org.moonshot.server.global.auth.jwt.JwtTokenProvider;
import org.moonshot.server.global.common.response.ApiResponse;
import org.moonshot.server.global.common.response.SuccessType;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/log")
public class LogController {

    private final LogService logService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> create(Principal principal, @RequestBody @Valid LogCreateRequestDto logCreateRequestDto) {
        Optional<AchieveResponseDto> response = logService.createRecordLog(JwtTokenProvider.getUserIdFromPrincipal(principal), logCreateRequestDto);
        if (response.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(SuccessType.POST_LOG_ACHIEVE_SUCCESS, response));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(SuccessType.POST_LOG_SUCCESS));
    }

}
