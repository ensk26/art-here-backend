package com.backend.arthere.image.application;

import com.backend.arthere.image.dto.ImageResponse;
import com.backend.arthere.image.dto.ImageUploadResponse;
import com.backend.arthere.image.util.PresignedURLUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    final private PresignedURLUtils presignedURLUtils;

    public ImageResponse createAdminUserImageSharePresignedURL(String imageURL) {

        String preSignedURL = presignedURLUtils.createImageShareURL(imageURL);
        return new ImageResponse(preSignedURL);
    }

    public ImageUploadResponse createAdminImageUploadPresignedURL() {

        String imageURL = "image/" + UUID.randomUUID().toString() + ".jpg";
        String preSignedURL = presignedURLUtils.createImageUploadURL(imageURL);

        return new ImageUploadResponse(preSignedURL, imageURL);
    }

    public ImageResponse createAdminDeletePresignedURL(String imageURL) {

        String preSignedURL = presignedURLUtils.createImageDeleteURL(imageURL);
        return new ImageResponse(preSignedURL);
    }
}
