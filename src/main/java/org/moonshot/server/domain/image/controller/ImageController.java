package org.moonshot.server.domain.image.controller;

import lombok.RequiredArgsConstructor;
import org.moonshot.server.domain.image.dto.request.NotifyImageSaveSuccessRequestDto;
import org.moonshot.server.domain.image.dto.response.PresignedUrlVO;
import org.moonshot.server.domain.image.service.ImageService;
import org.moonshot.server.global.common.response.ApiResponse;
import org.moonshot.server.global.common.response.SuccessType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    /**
     * 추후 로그인 유저를 확인하여 해당 유저에 대한 데이터로 getUploadPreSignedUrl로 username을 넘기는 로직으로 변경해야 함.
     */
    @GetMapping
    public ApiResponse<PresignedUrlVO> getPresignedUrl() {
        return ApiResponse.success(SuccessType.OK, imageService.getUploadPreSignedUrl("test", "SMC"));
    }

    /**
     * 해당 API도 username을 @AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : username")
     * 등을 이용하여 Annotation화 하여 바로 username을 넘길 수 있도록 변경해야 함.
     */
    @GetMapping("/notify")
    public ApiResponse<?> notifyImageSaveSuccess(@RequestBody NotifyImageSaveSuccessRequestDto request) {
        imageService.notifyImageSaveSuccess(request);
        return ApiResponse.success(SuccessType.OK);
    }
}
