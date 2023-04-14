package com.backend.arthere.arts.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ArtImageByLocationResponse {

    private Long id;

    private String artName;

    private String imageURL;

    private Double latitude;

    private Double longitude;

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
