package com.backend.arthere.arts.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ArtImageResponse {

    private Long id;

    private String artName;

    private String imageURL;

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
