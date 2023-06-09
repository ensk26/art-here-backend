package com.backend.arthere.post.domain;

import com.backend.arthere.post.dto.response.PostsResponse;

public interface PostCustomRepository {

    PostsResponse findPostsByArtsId(Long ArtsId, String sorting, String cursor);
}
