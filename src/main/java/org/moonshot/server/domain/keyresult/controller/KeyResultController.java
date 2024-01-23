package org.moonshot.server.domain.keyresult.controller;

import static org.moonshot.server.global.common.response.SuccessType.*;

import jakarta.validation.Valid;
import java.security.Principal;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.moonshot.server.domain.keyresult.dto.request.KeyResultCreateRequestDto;
import org.moonshot.server.domain.keyresult.dto.request.KeyResultModifyRequestDto;
import org.moonshot.server.domain.keyresult.service.KeyResultService;
import org.moonshot.server.domain.keyresult.dto.response.KRDetailResponseDto;
import org.moonshot.server.domain.log.dto.response.AchieveResponseDto;
import org.moonshot.server.global.auth.jwt.JwtTokenProvider;
import org.moonshot.server.global.common.response.MoonshotResponse;
import org.moonshot.server.global.common.response.SuccessType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/key-result")
public class KeyResultController implements KeyResultApi {

    private final KeyResultService keyResultService;

    @PostMapping
    public ResponseEntity<MoonshotResponse<?>> createKeyResult(final Principal principal, @RequestBody @Valid final KeyResultCreateRequestDto request) {
        keyResultService.createKeyResult(request, JwtTokenProvider.getUserIdFromPrincipal(principal));
        return ResponseEntity.status(HttpStatus.CREATED).body(MoonshotResponse.success(POST_KEY_RESULT_SUCCESS));
    }

    @DeleteMapping("/{keyResultId}")
    public ResponseEntity<?> deleteKeyResult(final Principal principal, @PathVariable("keyResultId") final Long keyResultId) {
        keyResultService.deleteKeyResult(keyResultId, JwtTokenProvider.getUserIdFromPrincipal(principal));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping
    public ResponseEntity<MoonshotResponse<?>> modifyKeyResult(final Principal principal, @RequestBody @Valid final KeyResultModifyRequestDto request) {
        Optional<AchieveResponseDto> response = keyResultService.modifyKeyResult(request, JwtTokenProvider.getUserIdFromPrincipal(principal));
        if (response.isPresent()) {
            return ResponseEntity.ok(MoonshotResponse.success(PATCH_KR_ACHIEVE_SUCCESS, response));
        }
        return ResponseEntity.ok(MoonshotResponse.success(PATCH_KEY_RESULT_SUCCESS));
    }

    @GetMapping("/{keyResultId}")
    public ResponseEntity<MoonshotResponse<KRDetailResponseDto>> getKRDetails(final Principal principal, @PathVariable("keyResultId") final Long keyResultId) {
        return ResponseEntity.ok(MoonshotResponse.success(SuccessType.GET_KR_DETAIL_SUCCESS, keyResultService.getKRDetails(JwtTokenProvider.getUserIdFromPrincipal(principal), keyResultId)));
    }

}
