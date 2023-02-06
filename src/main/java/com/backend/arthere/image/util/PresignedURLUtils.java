package com.backend.arthere.image.util;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import org.springframework.beans.factory.annotation.Value;

import java.net.URL;
import java.time.Instant;
import java.util.Date;

public class PresignedURLUtils {

    @Value("${aws.s3.region}")
    Regions clientRegion;

    @Value("${aws.s3.bucket}")
    String bucketName;

    @Value("${aws.credentials.accessKey}")
    String accessKey;

    @Value("${aws.credentials.secretKey}")
    String secretKey;


    public String createImageShareURL(String objectKey) {

        try {
            AmazonS3 s3Client = createS3Client();
            Date expiration = setExpiration();
            GeneratePresignedUrlRequest urlRequest = createPreSignedURLRequest(objectKey, expiration, HttpMethod.GET);
            URL preSignedURL = s3Client.generatePresignedUrl(urlRequest);

            return preSignedURL.toString();

        } catch (AmazonServiceException e) {
            e.printStackTrace();

        } catch (SdkClientException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String createImageUploadURL(String objectKey) {

        try {
            AmazonS3 s3Client = createS3Client();
            Date expiration = setExpiration();
            GeneratePresignedUrlRequest urlRequest = createPreSignedURLRequest(objectKey, expiration, HttpMethod.PUT);
            URL preSignedURL = s3Client.generatePresignedUrl(urlRequest);

            return preSignedURL.toString();

        } catch (AmazonServiceException e) {
            e.printStackTrace();

        } catch (SdkClientException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String createImageDeleteURL(String objectKey) {

        try {
            AmazonS3 s3Client = createS3Client();
            Date expiration = setExpiration();
            GeneratePresignedUrlRequest urlRequest = createPreSignedURLRequest(objectKey, expiration, HttpMethod.DELETE);
            URL preSignedURL = s3Client.generatePresignedUrl(urlRequest);

            return preSignedURL.toString();

        } catch (AmazonServiceException e) {
            e.printStackTrace();

        } catch (SdkClientException e) {
            e.printStackTrace();
        }
        return null;
    }

    private AmazonS3 createS3Client() {

        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        return AmazonS3ClientBuilder.standard()
                .withRegion(clientRegion)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }

    private Date setExpiration() {

        Date expiration = new Date();
        long expTimeMillis = Instant.now().toEpochMilli();
        expTimeMillis += 1000 * 60 * 60; //1시간
        expiration.setTime(expTimeMillis);

        return expiration;
    }

    private GeneratePresignedUrlRequest createPreSignedURLRequest(String objectKey, Date expiration, HttpMethod httpMethod) {

        return new GeneratePresignedUrlRequest(bucketName, objectKey)
                .withMethod(httpMethod)
                .withExpiration(expiration);
    }
}

