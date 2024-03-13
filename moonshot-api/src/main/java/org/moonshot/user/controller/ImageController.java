package org.moonshot.user.controller;

import lombok.RequiredArgsConstructor;
import org.moonshot.model.Logging;
import org.moonshot.response.MoonshotResponse;
import org.moonshot.response.SuccessType;
import org.moonshot.s3.S3Service;
import org.moonshot.s3.dto.request.NotifyImageSaveSuccessRequestDto;
import org.moonshot.s3.dto.response.PresignedUrlVO;
import org.moonshot.user.model.LoginUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/image")
@RequiredArgsConstructor
public class ImageController {

    private final S3Service s3Service;

    @GetMapping
    @Logging(item = "Image", action = "Get")
    public ResponseEntity<MoonshotResponse<PresignedUrlVO>> getPresignedUrl(@LoginUser Long userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(MoonshotResponse.success(SuccessType.GET_PRESIGNED_URL_SUCCESS, s3Service.getUploadPreSignedUrl("test", userId)));
    }

    @PostMapping
    @Logging(item = "Image", action = "Post")
    public ResponseEntity<MoonshotResponse<?>> notifyImageSaveSuccess(@LoginUser Long userId, @RequestBody final NotifyImageSaveSuccessRequestDto request) {
        s3Service.notifyImageSaveSuccess(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(MoonshotResponse.success(SuccessType.POST_NOTIFY_IMAGE_SAVE_SUCCESS));
    }

}
