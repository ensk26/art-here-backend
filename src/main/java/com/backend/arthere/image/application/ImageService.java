package com.backend.arthere.image.application;

import com.amazonaws.services.s3.AmazonS3;
import com.backend.arthere.image.dto.ImageResponse;
import com.backend.arthere.image.dto.ImageUploadResponse;
import com.backend.arthere.image.util.PresignedURLUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final PresignedURLUtils presignedURLUtils;

    private final AmazonS3 adminS3Client;

    private final String adminBucketName;

    private final AmazonS3 userS3Client;

    private final String userBucketName;

    public ImageResponse createImageSharePresignedURL(String imageURL) {

        String preSignedURL = presignedURLUtils.createImageShareURL(imageURL, adminS3Client, adminBucketName);
        return new ImageResponse(preSignedURL);
    }

    public ImageUploadResponse createAdminImageUploadPresignedURL() {

        String imageURL = "image/" + UUID.randomUUID().toString() + ".jpg";
        String preSignedURL = presignedURLUtils.createImageUploadURL(imageURL, adminS3Client, adminBucketName);

        return new ImageUploadResponse(preSignedURL, imageURL);
    }

    public ImageResponse createAdminDeletePresignedURL(String imageURL) {

        String preSignedURL = presignedURLUtils.createImageDeleteURL(imageURL, adminS3Client, adminBucketName);
        return new ImageResponse(preSignedURL);
    }

    public ImageUploadResponse createUserImageUploadPresignedURL() {
        String imageURL = "image/" + UUID.randomUUID().toString() + ".jpg";
        String preSignedURL = presignedURLUtils.createImageUploadURL(imageURL, userS3Client, userBucketName);

        return new ImageUploadResponse(preSignedURL, imageURL);
    }

    public ImageResponse createUsernDeletePresignedURL(String imageURL) {

        String preSignedURL = presignedURLUtils.createImageDeleteURL(imageURL, userS3Client, userBucketName);
        return new ImageResponse(preSignedURL);
    }
}
