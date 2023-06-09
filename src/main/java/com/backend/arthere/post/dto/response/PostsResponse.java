package com.backend.arthere.post.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostsResponse {

    private List<PostInfoResponse> PostInfo;

    private String cursor;
}
