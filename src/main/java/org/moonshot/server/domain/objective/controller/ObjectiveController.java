package org.moonshot.server.domain.objective.controller;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.moonshot.server.domain.objective.dto.request.ModifyObjectiveRequestDto;
import org.moonshot.server.domain.objective.dto.request.OKRCreateRequestDto;
import org.moonshot.server.domain.objective.dto.request.ObjectiveHistoryRequestDto;
import org.moonshot.server.domain.objective.dto.response.DashboardResponseDto;
import org.moonshot.server.domain.objective.dto.response.HistoryResponseDto;
import org.moonshot.server.domain.objective.service.ObjectiveService;
import org.moonshot.server.global.auth.jwt.JwtTokenProvider;
import org.moonshot.server.global.common.response.MoonshotResponse;
import org.moonshot.server.global.common.response.SuccessType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/objective")
public class ObjectiveController {

    private final ObjectiveService objectiveService;

    @PostMapping
    public ResponseEntity<MoonshotResponse<?>> createObjective(Principal principal, @RequestBody @Valid OKRCreateRequestDto request) {
        objectiveService.createObjective(JwtTokenProvider.getUserIdFromPrincipal(principal), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(MoonshotResponse.success(SuccessType.POST_OKR_SUCCESS));
    }

    @DeleteMapping("/{objectiveId}")
    public ResponseEntity<?> deleteObjective(Principal principal, @PathVariable("objectiveId") Long objectiveId) {
        objectiveService.deleteObjective(JwtTokenProvider.getUserIdFromPrincipal(principal), objectiveId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping
    public ResponseEntity<MoonshotResponse<?>> modifyObjective(Principal principal, @RequestBody ModifyObjectiveRequestDto request) {
        objectiveService.modifyObjective(JwtTokenProvider.getUserIdFromPrincipal(principal), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(MoonshotResponse.success(SuccessType.PATCH_OBJECTIVE_SUCCESS));
    }

    @GetMapping
    public ResponseEntity<MoonshotResponse<DashboardResponseDto>> getObjectiveInDashboard(Principal principal, @Nullable @RequestParam("objectiveId") Long objectiveId) {
        DashboardResponseDto response = objectiveService.getObjectiveInDashboard(JwtTokenProvider.getUserIdFromPrincipal(principal), objectiveId);
        return ResponseEntity.ok(MoonshotResponse.success(SuccessType.GET_OKR_LIST_SUCCESS, response));
    }

    @GetMapping("/history")
    public ResponseEntity<MoonshotResponse<HistoryResponseDto>> getObjectiveHistory(Principal principal, @RequestBody ObjectiveHistoryRequestDto request) {
        HistoryResponseDto response = objectiveService.getObjectiveHistory(
                JwtTokenProvider.getUserIdFromPrincipal(principal), request);
        return ResponseEntity.ok(MoonshotResponse.success(SuccessType.OK, response));
    }

}
