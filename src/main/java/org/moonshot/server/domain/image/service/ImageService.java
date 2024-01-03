package org.moonshot.server.domain.image.service;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import org.moonshot.server.domain.image.dto.request.NotifyImageSaveSuccessRequestDto;
import org.moonshot.server.domain.image.dto.response.PresignedUrlVO;
import org.moonshot.server.global.config.AWSConfig;
import org.moonshot.server.global.constants.AWSConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Component
public class ImageService {

    private final String bucketName;
    private final AWSConfig awsConfig;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSSS");

    public ImageService(@Value("${aws.s3-bucket-name}") final String bucketName,
                        AWSConfig awsConfig) {
        this.bucketName = bucketName;
        this.awsConfig = awsConfig;
    }

    public PresignedUrlVO getUploadPreSignedUrl(String prefix, String username) {
        final String fileName = generateFileName(username);
        final String key = prefix + "/" + fileName;

        S3Presigner preSigner = awsConfig.getS3Presigner();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        PutObjectPresignRequest preSignedUrlRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(AWSConstants.PRE_SIGNED_URL_EXPIRE_MINUTE))
                .putObjectRequest(putObjectRequest)
                .build();

        String url = preSigner.presignPutObject(preSignedUrlRequest).url().toString();

        return PresignedUrlVO.of(key, url);
    }

    public void notifyImageSaveSuccess(final NotifyImageSaveSuccessRequestDto request) {
        /**
         * 추후 User 엔티티 개발 후
         * username 아이디를 가진 User 정보에 profile image를 bucketName + key로 삽입하면 됨.
         * 이는 UserService로 위임하여 데이터 처리하도록 하면 됨.
         * 또한 기존의 S3에 저장되어 있던 이미지를 삭제해야 함.
         */
    }

    private String generateFileName(String username) {
        return username + "-" + simpleDateFormat.format(new Date());
    }
}
