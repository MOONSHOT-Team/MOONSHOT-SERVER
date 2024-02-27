package org.moonshot.keyresult.controller;


import static org.moonshot.response.SuccessType.PATCH_KEY_RESULT_SUCCESS;
import static org.moonshot.response.SuccessType.PATCH_KR_ACHIEVE_SUCCESS;
import static org.moonshot.response.SuccessType.POST_KEY_RESULT_SUCCESS;

import jakarta.validation.Valid;
import java.security.Principal;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.moonshot.jwt.JwtTokenProvider;
import org.moonshot.keyresult.dto.request.KeyResultCreateRequestDto;
import org.moonshot.keyresult.dto.request.KeyResultModifyRequestDto;
import org.moonshot.keyresult.dto.response.KRDetailResponseDto;
import org.moonshot.keyresult.service.KeyResultService;
import org.moonshot.log.dto.response.AchieveResponseDto;
import org.moonshot.model.Logging;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/key-result")
public class KeyResultController implements KeyResultApi {

    private final KeyResultService keyResultService;

    @PostMapping
    @Logging(item = "KeyResult", action = "Post")
    public ResponseEntity<MoonshotResponse<?>> createKeyResult(final Principal principal, @RequestBody @Valid final KeyResultCreateRequestDto request) {
        keyResultService.createKeyResult(request, JwtTokenProvider.getUserIdFromPrincipal(principal));
        return ResponseEntity.status(HttpStatus.CREATED).body(MoonshotResponse.success(POST_KEY_RESULT_SUCCESS));
    }

    @DeleteMapping("/{keyResultId}")
    @Logging(item = "KeyResult", action = "Delete")
    public ResponseEntity<?> deleteKeyResult(final Principal principal, @PathVariable("keyResultId") final Long keyResultId) {
        keyResultService.deleteKeyResult(keyResultId, JwtTokenProvider.getUserIdFromPrincipal(principal));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping
    @Logging(item = "KeyResult", action = "Patch")
    public ResponseEntity<MoonshotResponse<?>> modifyKeyResult(final Principal principal, @RequestBody @Valid final KeyResultModifyRequestDto request) {
        Optional<AchieveResponseDto> response = keyResultService.modifyKeyResult(request, JwtTokenProvider.getUserIdFromPrincipal(principal));
        if (response.isPresent()) {
            return ResponseEntity.ok(MoonshotResponse.success(PATCH_KR_ACHIEVE_SUCCESS, response));
        }
        return ResponseEntity.ok(MoonshotResponse.success(PATCH_KEY_RESULT_SUCCESS));
    }

    @GetMapping("/{keyResultId}")
    @Logging(item = "KeyResult", action = "Get")
    public ResponseEntity<MoonshotResponse<KRDetailResponseDto>> getKRDetails(final Principal principal, @PathVariable("keyResultId") final Long keyResultId) {
        return ResponseEntity.ok(MoonshotResponse.success(SuccessType.GET_KR_DETAIL_SUCCESS, keyResultService.getKRDetails(
                JwtTokenProvider.getUserIdFromPrincipal(principal), keyResultId)));
    }

}
