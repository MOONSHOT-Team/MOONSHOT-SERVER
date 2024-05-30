package org.moonshot.objective.controller;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.moonshot.model.Logging;
import org.moonshot.objective.dto.request.ModifyObjectiveRequestDto;
import org.moonshot.objective.dto.request.OKRCreateRequestDto;
import org.moonshot.objective.dto.response.DashboardResponseDto;
import org.moonshot.objective.dto.response.history.HistoryResponseDto;
import org.moonshot.objective.model.Category;
import org.moonshot.objective.model.Criteria;
import org.moonshot.objective.service.ObjectiveService;
import org.moonshot.response.MoonshotResponse;
import org.moonshot.response.SuccessType;
import org.moonshot.user.model.LoginUser;
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
    @Logging(item = "Objective", action = "Post")
    public ResponseEntity<MoonshotResponse<?>> createObjective(@LoginUser Long userId, @RequestBody @Valid final OKRCreateRequestDto request) {
        objectiveService.createObjective(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(MoonshotResponse.success(SuccessType.POST_OKR_SUCCESS));
    }

    @DeleteMapping("/{objectiveId}")
    @Logging(item = "Objective", action = "Delete")
    public ResponseEntity<MoonshotResponse<DashboardResponseDto>> deleteObjective(@LoginUser Long userId, @PathVariable("objectiveId") final Long objectiveId) {
        DashboardResponseDto response = objectiveService.deleteObjective(userId, objectiveId);
        return ResponseEntity.ok(MoonshotResponse.success(SuccessType.DELETE_OBJECTIVE_SUCCESS, response));
    }

    @PatchMapping
    @Logging(item = "Objective", action = "Patch")
    public ResponseEntity<?> modifyObjective(@LoginUser Long userId, @RequestBody final ModifyObjectiveRequestDto request) {
        objectiveService.modifyObjective(userId, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Logging(item = "Objective", action = "Get")
    public ResponseEntity<MoonshotResponse<DashboardResponseDto>> getObjectiveInDashboard(@LoginUser Long userId, @Nullable @RequestParam("objectiveId") final Long objectiveId) {
        DashboardResponseDto response = objectiveService.getObjectiveInDashboard(userId, objectiveId);
        return ResponseEntity.ok(MoonshotResponse.success(SuccessType.GET_OKR_LIST_SUCCESS, response));
    }

    @GetMapping("/history")
    @Logging(item = "Objective", action = "Get")
    public ResponseEntity<MoonshotResponse<HistoryResponseDto>> getObjectiveHistory(@LoginUser Long userId, @RequestParam(required = false, name = "year") final Integer year,
                                                                                    @RequestParam(required = false, name = "category") final Category category,
                                                                                    @RequestParam(required = false, name = "criteria") final Criteria criteria) {
        HistoryResponseDto response = objectiveService.getObjectiveHistory(userId, year, category, criteria);
        return ResponseEntity.ok(MoonshotResponse.success(SuccessType.OK, response));
    }

}
