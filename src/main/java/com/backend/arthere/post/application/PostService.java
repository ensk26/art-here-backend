package com.backend.arthere.post.application;

import com.amazonaws.services.s3.AmazonS3;
import com.backend.arthere.arts.domain.Arts;
import com.backend.arthere.arts.domain.ArtsRepository;
import com.backend.arthere.arts.exception.ArtsNotFoundException;
import com.backend.arthere.dislike.domain.DislikeRepository;
import com.backend.arthere.image.util.PresignedURLUtils;
import com.backend.arthere.like.domain.LikeRepository;
import com.backend.arthere.member.domain.Member;
import com.backend.arthere.member.domain.MemberRepository;
import com.backend.arthere.member.exception.MemberNotFoundException;
import com.backend.arthere.member.exception.NotWriterException;
import com.backend.arthere.post.domain.Post;
import com.backend.arthere.post.domain.PostRepository;
import com.backend.arthere.post.dto.request.AddPostsRequest;
import com.backend.arthere.post.dto.request.CreatePostsRequest;
import com.backend.arthere.post.dto.response.PostInfoResponse;
import com.backend.arthere.post.dto.response.PostResponse;
import com.backend.arthere.post.dto.response.PostsResponse;
import com.backend.arthere.post.exception.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    private final LikeRepository likeRepository;

    private final DislikeRepository dislikeRepository;

    private final MemberRepository memberRepository;

    private final ArtsRepository artsRepository;

    private final PresignedURLUtils presignedURLUtils;

    private final AmazonS3 userS3Client;

    private final String userBucketName;

    private final EntityManager em;

    @Transactional(readOnly = true)
    public PostResponse find(final Long id, final Long memberId) {
        Post post = findPost(id);
        return PostResponse.of(post, isLike(id, memberId), isDislike(id, memberId), memberId);
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

    public PostsResponse findPosts(Long artId, String sorting, String cursor) {
        PostsResponse posts = postRepository.findPostsByArtsId(artId, sorting, cursor);

        for (PostInfoResponse post : posts.getPostInfo()) {
            if (!post.getImageURL().isEmpty()) {
                post.setImageURL(presignedURLUtils.createImageShareURL(post.getImageURL(), userS3Client, userBucketName));
            }
        }
        return posts;
    }

    @Transactional
    public void createPost(CreatePostsRequest request, Long userId) {
        try {
            Post post = Post.builder()
                    .title(request.getTitle())
                    .content(request.getContent())
                    .imageURL(request.getImageURL())
                    .likeCount(0L)
                    .dislikeCount(0L)
                    .member(getMember(userId))
                    .arts(getArts(request.getArtId()))
                    .build();
            postRepository.save(post);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Member getMember(Long id) {
        try {
            return memberRepository.getReferenceById(id);
        } catch (EntityNotFoundException e) {
            throw new MemberNotFoundException();
        }
    }

    private Arts getArts(Long id) {
        try {
            return artsRepository.getReferenceById(id);
        } catch (Exception e) {
            throw new ArtsNotFoundException();
        }
    }

    @Transactional
    public void addPost(AddPostsRequest request, Long userId) throws Exception {
        checkWriter(request.getPostId(), userId);

        Post post = em.find(Post.class, request.getPostId());
        post.updateTitle(request.getTitle());
        post.updateContent(request.getContent());
        post.updateImageURL(request.getImageURL());
    }

    @Transactional
    public void deletePost(Long postId, Long userId) throws Exception {
        checkWriter(postId, userId);
        postRepository.deleteById(postId);
    }

    private void checkWriter(Long postId, Long userId) {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isEmpty()) {
            throw new PostNotFoundException();
        }
        if (!post.get().getMember().getId().equals(userId)) {
            throw new NotWriterException();
        }
    }
}
