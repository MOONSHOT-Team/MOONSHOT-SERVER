package org.moonshot.log.controller;

import jakarta.validation.Valid;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.moonshot.log.dto.request.LogCreateRequestDto;
import org.moonshot.log.dto.response.AchieveResponseDto;
import org.moonshot.log.service.LogService;
import org.moonshot.model.Logging;
import org.moonshot.response.MoonshotResponse;
import org.moonshot.response.SuccessType;
import org.moonshot.user.model.LoginUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/log")
public class LogController implements LogApi {

    private final LogService logService;

    @PostMapping
    @Logging(item = "Log", action = "Post")
    public ResponseEntity<MoonshotResponse<?>> create(@LoginUser Long userId, @RequestBody @Valid final LogCreateRequestDto logCreateRequestDto) {
        Optional<AchieveResponseDto> response = logService.createRecordLog(userId, logCreateRequestDto);

        if (response.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(MoonshotResponse.success(SuccessType.POST_LOG_ACHIEVE_SUCCESS, response));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(MoonshotResponse.success(SuccessType.POST_LOG_SUCCESS));
    }

}
