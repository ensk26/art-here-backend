package com.backend.arthere.post.application;

import com.backend.arthere.dislike.domain.DislikeRepository;
import com.backend.arthere.like.domain.LikeRepository;
import com.backend.arthere.post.domain.Post;
import com.backend.arthere.post.domain.PostRepository;
import com.backend.arthere.post.dto.response.PostResponse;
import com.backend.arthere.post.exception.PostNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.backend.arthere.fixture.MemberFixtures.회원;
import static com.backend.arthere.fixture.MemberFixtures.회원_아이디;
import static com.backend.arthere.fixture.PostFixtures.게시물;
import static com.backend.arthere.fixture.PostFixtures.게시물_아이디;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @Mock
    private PostRepository postRepository;
    @Mock
    private LikeRepository likeRepository;

    @Mock
    private DislikeRepository dislikeRepository;
    @InjectMocks
    private PostService postService;

    @Test
    @DisplayName("게시물 하나 조회한다.")
    public void 게시물_조회() throws Exception {
        //given
        Post post = 게시물(게시물_아이디, 회원(회원_아이디));

        given(postRepository.findPostWithMember(post.getId()))
                .willReturn(Optional.of(post));
        given(likeRepository.existsByMemberIdAndPostId(null, 게시물_아이디))
                .willReturn(false);
        given(dislikeRepository.existsByMemberIdAndPostId(null, 게시물_아이디))
                .willReturn(false);
        //when
        PostResponse postResponse = postService.find(post.getId(), null);

        //then
        assertAll(
                () -> assertThat(post.getContent()).isEqualTo(postResponse.getContent()),
                () -> assertThat(post.getMember().getId()).isEqualTo(postResponse.getWriter().getId())
        );
    }

    @Test
    @DisplayName("해당 게시물이 존재하지 않을 때 예외 처리한다.")
    public void 게시물_존재하지_않을때_예외_발생() throws Exception {
        //given
        given(postRepository.findPostWithMember(any()))
                .willReturn(Optional.empty());
        //when //then
        assertThatThrownBy(() -> postService.find(게시물_아이디, null))
                .isInstanceOf(PostNotFoundException.class);
    }
}