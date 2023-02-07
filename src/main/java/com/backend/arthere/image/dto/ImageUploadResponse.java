package com.backend.arthere.image.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ImageUploadResponse {

    private String preSignedURL;

    private String key;
}
