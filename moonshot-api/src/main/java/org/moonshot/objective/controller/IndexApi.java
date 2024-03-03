package org.moonshot.objective.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.moonshot.objective.dto.request.ModifyIndexRequestDto;
import org.moonshot.response.MoonshotResponse;
import org.moonshot.user.model.LoginUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Index", description = "Index 수정 API")
public interface IndexApi {

    @ApiResponses(value =  {
            @ApiResponse(responseCode = "204", description = "대상의 순서 수정을 성공하였습니다"),
            @ApiResponse(responseCode = "400", description = "정상적이지 않은 Objective 위치입니다\t\n정상적이지 않은 KeyResult 위치입니다\t\n정상적이지 않은 Task 위치입니다", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자입니다", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class))),
            @ApiResponse(responseCode = "403", description = "해당 자원에 접근 권한이 없습니다", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 Objective입니다\t\n존재하지 않는 KeyResult입니다\t\n존재하지 않는 Task입니다", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class)))
    })
    @Operation(summary = "Objective, KeyResult, Task Index 변경")
    public ResponseEntity<MoonshotResponse<?>> modifyIdx(@LoginUser Long userId, @RequestBody @Valid ModifyIndexRequestDto request);

}
