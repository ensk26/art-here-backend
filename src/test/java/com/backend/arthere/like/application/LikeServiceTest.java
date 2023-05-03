package com.backend.arthere.like.application;

import com.backend.arthere.like.domain.Like;
import com.backend.arthere.like.domain.LikeRepository;
import com.backend.arthere.member.domain.Member;
import com.backend.arthere.member.domain.MemberRepository;
import com.backend.arthere.member.exception.MemberNotFoundException;
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
class LikeServiceTest {

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private LikeService likeService;

    @Test
    @DisplayName("게시물의 좋아요가 1 증가한다.")
    public void 게시물_좋아요_추가() throws Exception {
        //given
        Member member = 회원(회원_아이디);
        Post post = 게시물(게시물_아이디, member);
        Like like = new Like(member, post);

        given(postRepository.findById(post.getId()))
                .willReturn(Optional.of(post));
        given(memberRepository.findById(member.getId()))
                .willReturn(Optional.of(member));
        given(likeRepository.existsByMemberIdAndPostId(member.getId(), post.getId()))
                .willReturn(false);
        given(likeRepository.save(any()))
                .willReturn(like);
        doNothing().when(postRepository).increaseLikeCount(post.getId());

        //when
        likeService.addLike(post.getId(), member.getId());

        //then
        assertAll(
                () -> verify(postRepository).findById(post.getId()),
                () -> verify(memberRepository).findById(member.getId()),
                () -> verify(likeRepository).existsByMemberIdAndPostId(member.getId(), post.getId()),
                () -> verify(postRepository).increaseLikeCount(post.getId())
        );
    }

    @Test
    @DisplayName("게시물이 존재하지 않을 때 예외가 발생한다.")
    public void 게시물이_존재하지_않을_때_예외_발생() throws Exception {
        //given
        given(postRepository.findById(게시물_아이디))
                .willThrow(PostNotFoundException.class);
        //when //then
        assertThatThrownBy(() -> likeService.addLike(게시물_아이디,회원_아이디))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    @DisplayName("게시물의 좋아요가 1 감소한다.")
    public void 게시물_좋아요_취소() throws Exception {
        //given
        Member member = 회원(회원_아이디);
        Post post = 게시물(게시물_아이디, member);

        given(postRepository.findById(post.getId()))
                .willReturn(Optional.of(post));
        given(memberRepository.findById(member.getId()))
                .willReturn(Optional.of(member));
        given(likeRepository.existsByMemberIdAndPostId(member.getId(), post.getId()))
                .willReturn(true);
        doNothing().when(likeRepository).deleteByMemberIdAndPostId(member.getId(),post.getId());
        doNothing().when(postRepository).decreaseLikeCount(post.getId());

        //when
        likeService.subtractLike(post.getId(), member.getId());

        //then
        assertAll(
                () -> verify(postRepository).findById(post.getId()),
                () -> verify(memberRepository).findById(member.getId()),
                () -> verify(likeRepository).existsByMemberIdAndPostId(member.getId(), post.getId()),
                () -> verify(postRepository).decreaseLikeCount(post.getId())
        );
    }

    @Test
    @DisplayName("회원이 존재하지 않을 때 예외가 발생한다.")
    public void 회원이_존재하지_않을_때_예외_발생() throws Exception {
        //given
        Post post = 게시물(게시물_아이디, 회원());
        given(postRepository.findById(게시물_아이디))
                .willReturn(Optional.of(post));
        given(memberRepository.findById(회원_아이디))
                .willThrow(new MemberNotFoundException());
        //when //then
        assertThatThrownBy(() -> likeService.addLike(게시물_아이디,회원_아이디))
                .isInstanceOf(MemberNotFoundException.class);
    }
}