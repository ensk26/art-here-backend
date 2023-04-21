package com.backend.arthere.image.presentation;

import com.backend.arthere.image.application.ImageService;
import com.backend.arthere.image.dto.ImageResponse;
import com.backend.arthere.image.dto.ImageUploadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class ImageController {

    final private ImageService imageService;

    @GetMapping("/image/share")
    public ResponseEntity<?> createImageSharePresignedURL(@RequestParam("image") String imageURL) {

        ImageResponse imageResponse = imageService.createImageSharePresignedURL(imageURL);
        return ResponseEntity.ok(imageResponse);
    }

    @GetMapping("/admin/image/upload")
    public ResponseEntity<?> createAdminImageUploadPresignedURL() {

        ImageUploadResponse uploadResponse = imageService.createAdminImageUploadPresignedURL();
        return ResponseEntity.ok(uploadResponse);
    }

    @GetMapping("/admin/image/delete")
    public ResponseEntity<?> createAdminDeletePresignedURL(@RequestParam("image") String imageURL) {

        ImageResponse imageResponse = imageService.createAdminDeletePresignedURL(imageURL);
        return ResponseEntity.ok(imageResponse);
    }

    @GetMapping("/user/image/upload")
    public ResponseEntity<ImageUploadResponse> createUserImageUploadPresignedURL() {

        ImageUploadResponse uploadResponse = imageService.createUserImageUploadPresignedURL();
        return ResponseEntity.ok(uploadResponse);
    }

    @GetMapping("/user/image/delete")
    public ResponseEntity<ImageResponse> createUserDeletePresignedURL(@RequestParam("image") String imageURL) {

        ImageResponse imageResponse = imageService.createUsernDeletePresignedURL(imageURL);
        return ResponseEntity.ok(imageResponse);
    }
}
