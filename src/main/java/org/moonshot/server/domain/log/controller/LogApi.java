package org.moonshot.server.domain.log.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.moonshot.server.domain.log.dto.request.LogCreateRequestDto;
import org.moonshot.server.global.common.response.MoonshotResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;

@Tag(name = "Log", description = "Log 관련 API")
public interface LogApi {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Log 생성을 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "Log 입력값은 이전 값과 동일할 수 없습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class))),
            @ApiResponse(responseCode = "403", description = "해당 자원에 접근 권한이 없습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class)))
    })
    @Operation(summary = "Log 생성")
    public ResponseEntity<MoonshotResponse<?>> create(Principal principal,
                                                      @Parameter(in = ParameterIn.DEFAULT, name = "TaskSingleCreateRequest", description = "task 추가 요청 body")
                                                      @RequestBody @Valid LogCreateRequestDto logCreateRequestDto);

}
