package com.backend.arthere.post.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostInfoResponse {

    private Long id;

    private String title;

    private String name;

    private String imageURL;

    private Long like;

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
