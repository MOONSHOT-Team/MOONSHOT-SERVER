package org.moonshot.server.domain.objective.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import java.security.Principal;
import org.moonshot.server.domain.objective.dto.request.ModifyObjectiveRequestDto;
import org.moonshot.server.domain.objective.dto.request.OKRCreateRequestDto;
import org.moonshot.server.domain.objective.dto.response.DashboardResponseDto;
import org.moonshot.server.domain.objective.dto.response.HistoryResponseDto;
import org.moonshot.server.domain.objective.model.Category;
import org.moonshot.server.domain.objective.model.Criteria;
import org.moonshot.server.global.common.response.MoonshotResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Objective", description = "Objective 관련 API")
interface ObjectiveApi {

    @ApiResponses(value =  {
            @ApiResponse(responseCode = "201", description = "O-KR을 생성을 성공하였습니다"),
            @ApiResponse(responseCode = "400", description = "허용된 Objective 개수를 초과하였습니다\t\nKeyResult 기간 설정이 Objective의 기간 내에 이루어지지 않습니다", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자입니다", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 유저입니다", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class)))
    })
    @Operation(summary = "O-KR 데이터 생성")
    ResponseEntity<MoonshotResponse<?>> createObjective(@Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Access Token", required = true, schema = @Schema(type = "string")) Principal principal,
                                                        @RequestBody @Valid OKRCreateRequestDto request);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "O-KR 트리 삭제를 성공하였습니다"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자입니다", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class))),
            @ApiResponse(responseCode = "403", description = "해당 자원에 접근 권한이 없습니다", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 Objective입니다", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class)))
    })
    @Operation(summary = "O-KR 데이터 삭제")
    ResponseEntity<MoonshotResponse<DashboardResponseDto>> deleteObjective(@Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Access Token", required = true, schema = @Schema(type = "string")) Principal principal,
                                      @PathVariable("objectiveId") Long objectiveId);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Objective 수정에 성공하였습니다"),
            @ApiResponse(responseCode = "400", description = "Objective 종료 기간은 오늘보다 이전 날짜일 수 없습니다", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자입니다", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class))),
            @ApiResponse(responseCode = "403", description = "해당 자원에 접근 권한이 없습니다", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 Objective입니다", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class)))
    })
    @Operation(summary = "Objective 데이터 수정")
    ResponseEntity<?> modifyObjective(@Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Access Token", required = true, schema = @Schema(type = "string")) Principal principal,
                                                        @RequestBody ModifyObjectiveRequestDto request);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "O-KR 목록 조회에 성공하였습니다"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자입니다", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class))),
            @ApiResponse(responseCode = "403", description = "해당 자원에 접근 권한이 없습니다", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 Objective입니다", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class)))
    })
    @Operation(summary = "O-KR 목록 조회")
    ResponseEntity<MoonshotResponse<DashboardResponseDto>> getObjectiveInDashboard(@Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "Access Token", required = true, schema = @Schema(type = "string")) Principal principal,
                                                                                   @Nullable @RequestParam("objectiveId") Long objectiveId);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "히스토리 조회에 성공하였습니다."),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자입니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class))),
            @ApiResponse(responseCode = "403", description = "해당 자원에 접근 권한이 없습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class))),
    })
    @Operation(summary = "히스토리 목록 조회")
    ResponseEntity<MoonshotResponse<HistoryResponseDto>> getObjectiveHistory(Principal principal, @RequestParam(required = false) Integer year,
                                                                                    @RequestParam(required = false) Category category,
                                                                                    @RequestParam(required = false) Criteria criteria);

}
