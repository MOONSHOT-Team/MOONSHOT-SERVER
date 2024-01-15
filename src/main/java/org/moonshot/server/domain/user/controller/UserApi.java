package org.moonshot.server.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import org.moonshot.server.domain.user.dto.request.SocialLoginRequest;
import org.moonshot.server.domain.user.dto.request.UserInfoRequest;
import org.moonshot.server.domain.user.dto.response.SocialLoginResponse;
import org.moonshot.server.domain.user.dto.response.UserInfoResponse;
import org.moonshot.server.domain.user.model.User;
import org.moonshot.server.global.auth.jwt.TokenResponse;
import org.moonshot.server.global.common.response.MoonshotResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "User", description = "유저 관련 API")
public interface UserApi {

    @ApiResponse(responseCode = "200", description = "로그인에 성공하였습니다.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class)))
    @Operation(summary = "소셜 회원가입 및 로그인",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
                    schema = @Schema(
                            allOf = { User.class },
                            requiredProperties = { "socialPlatform", "code" }
                    )
            )
    ))
    public ResponseEntity<MoonshotResponse<SocialLoginResponse>> login(@Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "인가 코드", required = true, schema = @Schema(type = "string")) @RequestHeader("Authorization") String authorization,
                                                                       @RequestBody SocialLoginRequest socialLoginRequest) throws IOException;

    @ApiResponse(responseCode = "200", description = "엑세스 토큰 재발급에 성공하였습니다.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class)))
    @Operation(summary = "액세스 토큰 재발급")
    public ResponseEntity<MoonshotResponse<TokenResponse>> reissue(@Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "인가 코드", required = true, schema = @Schema(type = "string")) @RequestHeader("Authorization") String refreshToken);


    @ApiResponse(responseCode = "200", description = "로그아웃에 성공하였습니다.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class)))
    @Operation(summary = "로그아웃")
    public ResponseEntity<MoonshotResponse<?>> logout(Principal principal);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "로그아웃에 성공하였습니다.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 유저입니다.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class)))
    })
    @Operation(summary = "로그아웃")
    public ResponseEntity<?> withdrawal(Principal principal);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "사용자 프로필 업데이트에 성공하였습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 유저입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class)))
    })
    @Operation(summary = "프로필 수정")
    public ResponseEntity<?> modifyProfile(Principal principal,
                                           @Parameter(in = ParameterIn.DEFAULT, name = "UserInfoRequest", description = "유저 정보 요청 body")
                                           @Valid @RequestBody UserInfoRequest userInfoRequest);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 프로필 조회에 성공하였습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 유저입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class)))
    })
    @Operation(summary = "프로필 조회")
    public ResponseEntity<MoonshotResponse<UserInfoResponse>> getMyProfile(Principal principal);

    @ApiResponse(responseCode = "200", description = "구글 로그인에 성공하였습니다.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class)))
    @Operation(summary = "구글 로그인")
    public String authTest(HttpServletRequest request, HttpServletResponse response);

}
