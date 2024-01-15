package org.moonshot.server.domain.task.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.moonshot.server.domain.task.dto.request.TaskSingleCreateRequestDto;
import org.moonshot.server.global.common.response.MoonshotResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;

@Tag(name = "Task", description = "Task 관련 API")
public interface TaskApi {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task 생성을 성공하였습니다"),
            @ApiResponse(responseCode = "400", description = "1.해당 자원에 접근 권한이 없습니다\t\n2.허용된 Task 개수를 초과하였습니다\t\n3.정상적이지 않은 KeyResult 위치입니다\t\n",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class)))
    })
    @Operation(summary = "Task 추가")
    public ResponseEntity<MoonshotResponse<?>> createTask(Principal principal,
                                                          @Parameter(in = ParameterIn.DEFAULT, name = "TaskSingleCreateRequest", description = "task 추가 요청 body")
                                                          @RequestBody @Valid TaskSingleCreateRequestDto request);
}



