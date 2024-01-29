package org.moonshot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.SystemPropertyCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class AWSConfig {

    private static final String AWS_ACCESS_KEY_ID = "aws.accessKeyId";
    private static final String AWS_SECRET_ACCESS_KEY = "aws.secretAccessKey";

    private final String accessKey;
    private final String secretKey;
    private final String regionValue;

    public AWSConfig(@Value("${aws.access-key}") final String accessKey,
                     @Value("${aws.secret-key}") final String secretKey,
                     @Value("${aws.region}") final String regionValue) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.regionValue = regionValue;
    }

    @Bean
    public SystemPropertyCredentialsProvider systemPropertyCredentialsProvider() {
        System.setProperty(AWS_ACCESS_KEY_ID, accessKey);
        System.setProperty(AWS_SECRET_ACCESS_KEY, secretKey);
        return SystemPropertyCredentialsProvider.create();
    }

    @Bean
    public Region getRegion() {
        return Region.of(regionValue);
    }

    @Bean
    public S3Client getS3Client() {
        return S3Client.builder()
                .region(getRegion())
                .credentialsProvider(systemPropertyCredentialsProvider())
                .build();
    }

    @Bean
    public S3Presigner getS3Presigner() {
        return S3Presigner.builder()
                .region(getRegion())
                .credentialsProvider(systemPropertyCredentialsProvider())
                .build();
    }

}
