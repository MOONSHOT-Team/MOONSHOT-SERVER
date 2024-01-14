package org.moonshot.server.domain.objective.controller;

import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.moonshot.server.domain.objective.dto.request.ModifyIndexRequestDto;
import org.moonshot.server.domain.objective.model.IndexService;
import org.moonshot.server.domain.objective.service.IndexTargetProvider;
import org.moonshot.server.global.auth.jwt.JwtTokenProvider;
import org.moonshot.server.global.common.response.ApiResponse;
import org.moonshot.server.global.common.response.SuccessType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/index")
public class IndexController {

    private final IndexTargetProvider indexTargetProvider;

    @PatchMapping
    public ResponseEntity<ApiResponse<?>> modifyIdx(Principal principal, @RequestBody @Valid ModifyIndexRequestDto request) {
        IndexService indexService = indexTargetProvider.getIndexService(request.target());
        indexService.modifyIdx(request, JwtTokenProvider.getUserIdFromPrincipal(principal));
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(SuccessType.PATCH_TARGET_INDEX_SUCCESS));
    }

}
