package org.moonshot.objective.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.moonshot.objective.dto.request.ModifyIndexRequestDto;
import org.moonshot.objective.model.IndexService;
import org.moonshot.objective.service.IndexTargetProvider;
import org.moonshot.response.MoonshotResponse;
import org.moonshot.user.model.LoginUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/index")
public class IndexController implements IndexApi {

    private final IndexTargetProvider indexTargetProvider;

    @PatchMapping
    public ResponseEntity<MoonshotResponse<?>> modifyIdx(@LoginUser Long userId, @RequestBody @Valid final ModifyIndexRequestDto request) {
        IndexService indexService = indexTargetProvider.getIndexService(request.target());
        indexService.modifyIdx(request, userId);
        return ResponseEntity.noContent().build();
    }

}
