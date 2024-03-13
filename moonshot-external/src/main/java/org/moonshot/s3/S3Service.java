package org.moonshot.s3;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import org.moonshot.config.AWSConfig;
import org.moonshot.constants.AWSConstants;
import org.moonshot.s3.dto.request.NotifyImageSaveSuccessRequestDto;
import org.moonshot.s3.dto.response.PresignedUrlVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Component
public class S3Service {

    private final String bucketName;
    private final AWSConfig awsConfig;
    private final ApplicationEventPublisher eventPublisher;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSSS");

    public S3Service(@Value("${aws.s3-bucket-name}") final String bucketName,
                     AWSConfig awsConfig, ApplicationEventPublisher eventPublisher) {
        this.bucketName = bucketName;
        this.awsConfig = awsConfig;
        this.eventPublisher = eventPublisher;
    }

    public PresignedUrlVO getUploadPreSignedUrl(final String prefix, final Long userId) {
        final String fileName = generateFileName(userId);
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

    @Transactional
    @Retryable(maxAttempts = 3, backoff = @Backoff(2000))
    public void notifyImageSaveSuccess(final Long userId, final NotifyImageSaveSuccessRequestDto request) {
        String imageUrl = getImageUrl(request.fileName());
        publishImageEvent(userId, imageUrl, request.imageType());
    }

    private String generateFileName(final Long userId) {
        return userId + "-" + simpleDateFormat.format(new Date());
    }

    private String getImageUrl(final String fileName) {
        return "https://" + bucketName + ".s3.ap-northeast-2.amazonaws.com/" + fileName;
    }

    private void publishImageEvent(final Long userId, final String imageUrl, final ImageType imageType) {
        eventPublisher.publishEvent(ImageEvent.of(userId, imageUrl, imageType));
    }

}
