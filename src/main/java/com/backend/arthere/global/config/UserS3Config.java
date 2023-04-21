package com.backend.arthere.global.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserS3Config {

    @Value("${aws.user.s3.region}")
    private Regions clientRegion;

    @Value("${aws.user.s3.bucket}")
    private String bucketName;

    @Value("${aws.user.credentials.accessKey}")
    private String accessKey;

    @Value("${aws.user.credentials.secretKey}")
    private String secretKey;


    @Bean(name = "userS3Client")
    public AmazonS3 createS3Client() {

        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        return AmazonS3ClientBuilder.standard()
                .withRegion(clientRegion)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }

    @Bean(name = "userBucketName")
    public String getBucketName() {
        return bucketName;
    }
}
