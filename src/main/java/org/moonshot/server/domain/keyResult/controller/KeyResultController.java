package org.moonshot.server.domain.keyresult.controller;

import static org.moonshot.server.global.common.response.SuccessType.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.moonshot.server.domain.keyresult.dto.request.KeyResultCreateRequestDto;
import org.moonshot.server.domain.keyresult.dto.request.KeyResultModifyRequestDto;
import org.moonshot.server.domain.keyresult.service.KeyResultService;
import org.moonshot.server.global.common.response.ApiResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/key-result")
public class KeyResultController {

    private final KeyResultService keyResultService;

    @PostMapping
    public ApiResponse<?> createKeyResult(@RequestBody @Valid KeyResultCreateRequestDto request) {
        keyResultService.createKeyResult(request);
        return ApiResponse.success(POST_KEY_RESULT_SUCCESS);
    }

    @DeleteMapping
    public ApiResponse<?> deleteKeyResult(@RequestParam Long keyResultId) {
        keyResultService.deleteKeyResult(keyResultId);
        return ApiResponse.success(DELETE_KEY_RESULT_SUCCESS);
    }

    @PatchMapping
    public ApiResponse<?> modifyKeyResult(@RequestBody @Valid KeyResultModifyRequestDto request) {
        keyResultService.modifyKeyResult(request);
        return ApiResponse.success(PATCH_KEY_RESULT_SUCCESS);
    }

}
