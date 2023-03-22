package com.backend.arthere.image.util;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.Instant;
import java.util.Date;


@Component
public class PresignedURLUtils {

    public String createImageShareURL(String objectKey, AmazonS3 s3Client, String bucketName) {

        try {
            Date expiration = setExpiration();
            GeneratePresignedUrlRequest urlRequest = createPreSignedURLRequest(objectKey, expiration,
                    HttpMethod.GET, bucketName);
            URL preSignedURL = s3Client.generatePresignedUrl(urlRequest);

            return preSignedURL.toString();

        } catch (AmazonServiceException e) {
            e.printStackTrace();

        } catch (SdkClientException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String createImageUploadURL(String objectKey, AmazonS3 s3Client, String bucketName) {

        try {
            Date expiration = setExpiration();
            GeneratePresignedUrlRequest urlRequest = createPreSignedURLRequest(objectKey, expiration,
                    HttpMethod.PUT, bucketName);
            URL preSignedURL = s3Client.generatePresignedUrl(urlRequest);

            return preSignedURL.toString();

        } catch (AmazonServiceException e) {
            e.printStackTrace();

        } catch (SdkClientException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String createImageDeleteURL(String objectKey, AmazonS3 s3Client, String bucketName) {

        try {
            Date expiration = setExpiration();
            GeneratePresignedUrlRequest urlRequest = createPreSignedURLRequest(objectKey, expiration,
                    HttpMethod.DELETE, bucketName);
            URL preSignedURL = s3Client.generatePresignedUrl(urlRequest);

            return preSignedURL.toString();

        } catch (AmazonServiceException e) {
            e.printStackTrace();

        } catch (SdkClientException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Date setExpiration() {

        Date expiration = new Date();
        long expTimeMillis = Instant.now().toEpochMilli();
        expTimeMillis += 1000 * 60 * 60; //1시간
        expiration.setTime(expTimeMillis);

        return expiration;
    }

    private GeneratePresignedUrlRequest createPreSignedURLRequest(String objectKey, Date expiration,
                                                                  HttpMethod httpMethod, String bucketName) {

        return new GeneratePresignedUrlRequest(bucketName, objectKey)
                .withMethod(httpMethod)
                .withExpiration(expiration);
    }
}

