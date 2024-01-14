package org.moonshot.server.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.moonshot.server.global.external.s3.dto.request.NotifyImageSaveSuccessRequestDto;
import org.moonshot.server.global.external.s3.dto.response.PresignedUrlVO;
import org.moonshot.server.global.external.s3.service.S3Service;
import org.moonshot.server.global.common.response.MoonshotResponse;
import org.moonshot.server.global.common.response.SuccessType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class ImageController {

    private final S3Service s3Service;

    //TODO
    // 추후 로그인 유저를 확인하여 해당 유저에 대한 데이터로 getUploadPreSignedUrl로 username을 넘기는 로직으로 변경해야 함.
    @GetMapping("/image")
    public ResponseEntity<MoonshotResponse<PresignedUrlVO>> getPresignedUrl() {
        return ResponseEntity.status(HttpStatus.OK).body(
                MoonshotResponse.success(SuccessType.GET_PRESIGNED_URL_SUCCESS, s3Service.getUploadPreSignedUrl("test", "SMC")));
    }

    //TODO
    // 해당 API도 username을 @AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : username")
    // 등을 이용하여 Annotation화 하여 바로 username을 넘길 수 있도록 변경해야 함.
    @PostMapping("/image")
    public ResponseEntity<MoonshotResponse<?>> notifyImageSaveSuccess(@RequestBody NotifyImageSaveSuccessRequestDto request) {
        s3Service.notifyImageSaveSuccess(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(MoonshotResponse.success(SuccessType.POST_NOTIFY_IMAGE_SAVE_SUCCESS));
    }

}
