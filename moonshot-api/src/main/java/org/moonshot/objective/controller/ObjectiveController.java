package org.moonshot.objective.controller;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.moonshot.jwt.JwtTokenProvider;
import org.moonshot.objective.dto.request.ModifyObjectiveRequestDto;
import org.moonshot.objective.dto.request.OKRCreateRequestDto;
import org.moonshot.objective.dto.response.DashboardResponseDto;
import org.moonshot.objective.dto.response.HistoryResponseDto;
import org.moonshot.objective.model.Category;
import org.moonshot.objective.model.Criteria;
import org.moonshot.objective.service.ObjectiveService;
import org.moonshot.response.MoonshotResponse;
import org.moonshot.response.SuccessType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/objective")
public class ObjectiveController implements ObjectiveApi {

    private final ObjectiveService objectiveService;

    @PostMapping
    public ResponseEntity<MoonshotResponse<?>> createObjective(final Principal principal, @RequestBody @Valid final OKRCreateRequestDto request) {
        objectiveService.createObjective(JwtTokenProvider.getUserIdFromPrincipal(principal), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(MoonshotResponse.success(SuccessType.POST_OKR_SUCCESS));
    }

    @DeleteMapping("/{objectiveId}")
    public ResponseEntity<MoonshotResponse<DashboardResponseDto>> deleteObjective(final Principal principal, @PathVariable("objectiveId") final Long objectiveId) {
        DashboardResponseDto response = objectiveService.deleteObjective(JwtTokenProvider.getUserIdFromPrincipal(principal), objectiveId);
        return ResponseEntity.ok(MoonshotResponse.success(SuccessType.DELETE_OBJECTIVE_SUCCESS, response));
    }

    @PatchMapping
    public ResponseEntity<?> modifyObjective(final Principal principal, @RequestBody final ModifyObjectiveRequestDto request) {
        objectiveService.modifyObjective(JwtTokenProvider.getUserIdFromPrincipal(principal), request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<MoonshotResponse<DashboardResponseDto>> getObjectiveInDashboard(final Principal principal, @Nullable @RequestParam("objectiveId") final Long objectiveId) {
        DashboardResponseDto response = objectiveService.getObjectiveInDashboard(JwtTokenProvider.getUserIdFromPrincipal(principal), objectiveId);
        return ResponseEntity.ok(MoonshotResponse.success(SuccessType.GET_OKR_LIST_SUCCESS, response));
    }

    @GetMapping("/history")
    public ResponseEntity<MoonshotResponse<HistoryResponseDto>> getObjectiveHistory(final Principal principal, @RequestParam(required = false) final Integer year,
                                                                                    @RequestParam(required = false) final Category category,
                                                                                    @RequestParam(required = false) final Criteria criteria) {
        HistoryResponseDto response = objectiveService.getObjectiveHistory(JwtTokenProvider.getUserIdFromPrincipal(principal), year, category, criteria);
        return ResponseEntity.ok(MoonshotResponse.success(SuccessType.OK, response));
    }

}
