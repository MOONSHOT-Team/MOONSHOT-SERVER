package org.moonshot.keyresult.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.moonshot.keyresult.dto.request.KeyResultCreateRequestDto;
import org.moonshot.keyresult.dto.request.KeyResultModifyRequestDto;
import org.moonshot.keyresult.dto.response.KRDetailResponseDto;
import org.moonshot.response.MoonshotResponse;
import org.moonshot.user.model.LoginUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "KeyResult", description = "KeyResult 관련 API")
public interface KeyResultApi {

    @ApiResponses(value =  {
            @ApiResponse(responseCode = "201", description = "KeyResult 생성을 성공하였습니다"),
            @ApiResponse(responseCode = "400", description = "허용된 Key Result 개수를 초과하였습니다\t\n정상적이지 않은 KeyResult 위치입니다", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자입니다", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class))),
            @ApiResponse(responseCode = "403", description = "해당 자원에 접근 권한이 없습니다", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 Objective입니다\t\n존재하지 않는 KeyResult입니다", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class)))
    })
    @Operation(summary = "KeyResult 데이터 생성")
    public ResponseEntity<MoonshotResponse<?>> createKeyResult(@LoginUser Long userId, @RequestBody @Valid KeyResultCreateRequestDto request);

    @ApiResponses(value =  {
            @ApiResponse(responseCode = "204", description = "KeyResult 삭제를 성공하였습니다"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자입니다", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class))),
            @ApiResponse(responseCode = "403", description = "해당 자원에 접근 권한이 없습니다", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 KeyResult입니다", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class)))
    })
    @Operation(summary = "KeyResult 데이터 삭제")
    public ResponseEntity<?> deleteKeyResult(@LoginUser Long userId, @PathVariable("keyResultId") Long keyResultId);

    @ApiResponses(value =  {
            @ApiResponse(responseCode = "200", description = "KeyResult 수정 후 목표를 달성하였습니다\t\nKeyResult 수정을 성공하였습니다"),
            @ApiResponse(responseCode = "400", description = "Log 입력값은 이전 값과 동일할 수 없습니다\t\nKeyResult 기간 설정이 올바르지 않습니다\t\n", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자입니다", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class))),
            @ApiResponse(responseCode = "403", description = "해당 자원에 접근 권한이 없습니다", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 KeyResult입니다\t\n존재하지 않는 Log입니다", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class)))
    })
    @Operation(summary = "KeyResult 데이터 수정")
    public ResponseEntity<MoonshotResponse<?>> modifyKeyResult(@LoginUser Long userId, @RequestBody @Valid KeyResultModifyRequestDto request);

    @ApiResponses(value =  {
            @ApiResponse(responseCode = "201", description = "O-KR을 생성을 성공하였습니다"),
            @ApiResponse(responseCode = "400", description = "허용된 Objective 개수를 초과하였습니다", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자입니다", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class))),
            @ApiResponse(responseCode = "403", description = "해당 자원에 접근 권한이 없습니다", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 유저입니다", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class)))
    })
    @Operation(summary = "KeyResult 상세 조회 (사이드바)")
    public ResponseEntity<MoonshotResponse<KRDetailResponseDto>> getKRDetails(@LoginUser Long userId, @PathVariable("keyResultId") Long keyResultId);
}
