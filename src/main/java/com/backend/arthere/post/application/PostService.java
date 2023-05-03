package com.backend.arthere.post.application;

import com.amazonaws.services.s3.AmazonS3;
import com.backend.arthere.dislike.domain.DislikeRepository;
import com.backend.arthere.image.util.PresignedURLUtils;
import com.backend.arthere.like.domain.LikeRepository;
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

    private final LikeRepository likeRepository;

    private final DislikeRepository dislikeRepository;

    private final PresignedURLUtils presignedURLUtils;

    private final AmazonS3 userS3Client;

    private final String userBucketName;

    @Transactional(readOnly = true)
    public PostResponse find(final Long id, final Long memberId) {
        Post post = findPost(id);
        return PostResponse.of(post, isLike(id, memberId), isDislike(id, memberId));
    }

    private boolean isLike(final Long id, final Long memberId) {
        return likeRepository.existsByMemberIdAndPostId(memberId, id);
    }

    private boolean isDislike(final Long id, final Long memberId) {
        return dislikeRepository.existsByMemberIdAndPostId(memberId, id);
    }

    private Post findPost(final Long id) {
        return postRepository.findPostWithMember(id)
                .orElseThrow(PostNotFoundException::new);
    }
}
