package com.backend.arthere.post.application;

import com.amazonaws.services.s3.AmazonS3;
import com.backend.arthere.image.util.PresignedURLUtils;
import com.backend.arthere.post.domain.Post;
import com.backend.arthere.post.domain.PostRepository;
import com.backend.arthere.post.dto.response.PostResponse;
import com.backend.arthere.post.exception.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    private final PresignedURLUtils presignedURLUtils;

    private final AmazonS3 userS3Client;

    private final String userBucketName;

    @Transactional(readOnly = true)
    public PostResponse find(final Long id) {
        Post post = findPost(id);
        return PostResponse.from(post);
    }

    private Post findPost(final Long id) {
        return postRepository.findPostWithMember(id)
                .orElseThrow(PostNotFoundException::new);
    }
}
