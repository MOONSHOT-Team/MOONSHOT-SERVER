package org.moonshot.server.domain.objective.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.moonshot.server.domain.objective.dto.request.OKRCreateRequestDto;
import org.moonshot.server.domain.objective.service.ObjectiveService;
import org.moonshot.server.global.common.response.ApiResponse;
import org.moonshot.server.global.common.response.SuccessType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/objective")
public class ObjectiveController {

    private final ObjectiveService objectiveService;

    @PostMapping
    public ApiResponse<?> create(@RequestBody @Valid OKRCreateRequestDto request, String nickname) {
        final String TEST_USER_NICKNAME = "tester";
        objectiveService.create(request, TEST_USER_NICKNAME);
        return ApiResponse.success(SuccessType.POST_OKR_SUCCESS);
    }

}
