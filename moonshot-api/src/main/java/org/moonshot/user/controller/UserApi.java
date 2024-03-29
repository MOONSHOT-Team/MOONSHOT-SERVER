package org.moonshot.user.controller;

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
import org.moonshot.jwt.TokenResponse;
import org.moonshot.response.MoonshotResponse;
import org.moonshot.user.dto.request.SocialLoginRequest;
import org.moonshot.user.dto.request.UserInfoRequest;
import org.moonshot.user.dto.response.SocialLoginResponse;
import org.moonshot.user.dto.response.UserInfoResponse;
import org.moonshot.user.model.LoginUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "User", description = "유저 관련 API")
public interface UserApi {

    @ApiResponse(responseCode = "200", description = "로그인에 성공하였습니다.")
    @Operation(summary = "소셜 회원가입 및 로그인")
    public ResponseEntity<MoonshotResponse<SocialLoginResponse>> login(@Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "인가 코드", required = true, schema = @Schema(type = "string")) @RequestHeader("Authorization") String authorization,
                                                                       @RequestBody SocialLoginRequest socialLoginRequest) throws IOException;

    @ApiResponse(responseCode = "200", description = "엑세스 토큰 재발급에 성공하였습니다.")
    @Operation(summary = "액세스 토큰 재발급")
    public ResponseEntity<MoonshotResponse<TokenResponse>> reissue(@Parameter(in = ParameterIn.HEADER, name = "Authorization", description = "인가 코드", required = true, schema = @Schema(type = "string")) @RequestHeader("Authorization") String refreshToken);


    @ApiResponse(responseCode = "200", description = "로그아웃에 성공하였습니다.")
    @Operation(summary = "로그아웃")
    public ResponseEntity<MoonshotResponse<?>> logout(@LoginUser Long userId);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "로그아웃에 성공하였습니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 유저입니다.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class)))
    })
    @Operation(summary = "로그아웃")
    public ResponseEntity<?> withdrawal(@LoginUser Long userId);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "사용자 프로필 업데이트에 성공하였습니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 유저입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class)))
    })
    @Operation(summary = "프로필 수정")
    public ResponseEntity<?> modifyProfile(@LoginUser Long userId,
                                           @Parameter(in = ParameterIn.DEFAULT, name = "UserInfoRequest", description = "유저 정보 요청 body")
                                           @Valid @RequestBody UserInfoRequest userInfoRequest);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 프로필 조회에 성공하였습니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 유저입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoonshotResponse.class)))
    })
    @Operation(summary = "프로필 조회")
    public ResponseEntity<MoonshotResponse<UserInfoResponse>> getMyProfile(@LoginUser Long userId);

}
