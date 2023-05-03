package com.backend.arthere.dislike.application;

import com.backend.arthere.dislike.domain.Dislike;
import com.backend.arthere.dislike.domain.DislikeRepository;
import com.backend.arthere.member.domain.Member;
import com.backend.arthere.member.domain.MemberRepository;
import com.backend.arthere.post.domain.Post;
import com.backend.arthere.post.domain.PostRepository;
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
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class DislikeServiceTest {
    @Mock
    private DislikeRepository dislikeRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private DislikeService dislikeService;

    @Test
    @DisplayName("게시물의 싫어요를 추가한다.")
    public void 게시물_싫어요_추가() throws Exception {
        //given
        Member member = 회원(회원_아이디);
        Post post = 게시물(게시물_아이디, member);
        Dislike dislike = new Dislike(member, post);

        given(postRepository.findById(post.getId()))
                .willReturn(Optional.of(post));
        given(memberRepository.findById(member.getId()))
                .willReturn(Optional.of(member));
        given(dislikeRepository.existsByMemberIdAndPostId(member.getId(), post.getId()))
                .willReturn(false);
        given(dislikeRepository.save(any()))
                .willReturn(dislike);
        doNothing().when(postRepository).increaseDislikeCount(post.getId());
        //when
        dislikeService.addDislike(post.getId(), member.getId());
        //then
        assertAll(
                () -> verify(postRepository).findById(post.getId()),
                () -> verify(memberRepository).findById(member.getId()),
                () -> verify(dislikeRepository).existsByMemberIdAndPostId(member.getId(), post.getId()),
                () -> verify(postRepository).increaseDislikeCount(post.getId())
        );
    }

    @Test
    @DisplayName("게시물이 존재하지 않을 때 예외가 발생한다.")
    public void 게시물이_존재하지_않을_때_예외_발생() throws Exception {
        //given
        given(postRepository.findById(게시물_아이디))
                .willThrow(PostNotFoundException.class);
        //when //then
        assertThatThrownBy(() -> dislikeService.addDislike(게시물_아이디,회원_아이디))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    @DisplayName("게시물의 싫어요를 취소한다.")
    public void 게시물_싫어요_취소() throws Exception {
        //given
        Member member = 회원(회원_아이디);
        Post post = 게시물(게시물_아이디, member);
        Dislike dislike = new Dislike(member, post);

        given(postRepository.findById(post.getId()))
                .willReturn(Optional.of(post));
        given(memberRepository.findById(member.getId()))
                .willReturn(Optional.of(member));
        given(dislikeRepository.existsByMemberIdAndPostId(member.getId(), post.getId()))
                .willReturn(true);
        doNothing().when(dislikeRepository).deleteByMemberIdAndPostId(member.getId(), post.getId());
        doNothing().when(postRepository).decreaseDislikeCount(post.getId());
        //when
        dislikeService.subtractDislike(post.getId(), member.getId());

        //then
        assertAll(
                () -> verify(postRepository).findById(post.getId()),
                () -> verify(memberRepository).findById(member.getId()),
                () -> verify(dislikeRepository).existsByMemberIdAndPostId(member.getId(), post.getId()),
                () -> verify(postRepository).decreaseDislikeCount(post.getId())
        );
    }
}