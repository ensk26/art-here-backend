package com.backend.arthere.post.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostsRequest {

    @NotNull
    private Long artId;

    @NotNull
    @Size(max = 300)
    private String title;

    @NotNull
    @Size(max = 300)
    private String content;

    private String imageURL;
}
