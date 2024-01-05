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
    private final String TEST_USER_NICKNAME = "tester";

    @PostMapping
    public ApiResponse<?> createObjective(@RequestBody @Valid OKRCreateRequestDto request) {
        objectiveService.createObjective(request, TEST_USER_NICKNAME);
        return ApiResponse.success(SuccessType.POST_OKR_SUCCESS);
    }

    //TODO
    // PATCH API (목표 히스토리로 넘기기 - 상태수정)

}
