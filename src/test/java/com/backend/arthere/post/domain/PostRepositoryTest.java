package com.backend.arthere.post.domain;

import com.backend.arthere.global.config.JpaConfig;
import com.backend.arthere.global.config.QueryDslConfig;
import com.backend.arthere.member.domain.Member;
import com.backend.arthere.member.domain.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static com.backend.arthere.fixture.MemberFixtures.회원;
import static com.backend.arthere.fixture.PostFixtures.게시물;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@DataJpaTest
@Import({QueryDslConfig.class, JpaConfig.class})
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;
    private Post post;

    @BeforeEach
    void setUp() {
        member = 회원();
        memberRepository.save(member);

        post = 게시물(member, null);
        postRepository.save(post);
    }

    @Test
    @DisplayName("게시물을 조회한다.")
    public void 게시물_조회() throws Exception {
        //given
        //when
        Optional<Post> result = postRepository.findPostWithMember(post.getId());

        //then
        assertAll(
                () -> assertThat(result.get().getId()).isEqualTo(post.getId()),
                () -> assertThat(result.get().getTitle()).isEqualTo(post.getTitle()),
                () -> assertThat(result.get().getMember().getId()).isEqualTo(member.getId())
        );
    }

    @Test
    @DisplayName("게시물의 좋아요를 1 증가한다.")
    public void 좋아요_증가() throws Exception {
        //given
        //when
        postRepository.increaseLikeCount(post.getId());
        Optional<Post> result = postRepository.findById(post.getId());
        //then
        assertThat(result.get().getLikeCount()).isEqualTo(post.getLikeCount()+1);
    }

    @Test
    @DisplayName("게시물의 좋아요를 1 감소한다.")
    public void 좋아요_감소() throws Exception {
        //given
        //when
        postRepository.decreaseLikeCount(post.getId());
        Optional<Post> result = postRepository.findById(post.getId());
        //then
        assertThat(result.get().getLikeCount()).isEqualTo(post.getLikeCount()-1);
    }

    @Test
    @DisplayName("게시물의 싫어요를 1 증가시킨다.")
    public void 싫어요_증가() throws Exception {
        //given
        //when
        postRepository.increaseDislikeCount(post.getId());
        Optional<Post> result = postRepository.findById(post.getId());
        //then
        assertThat(result.get().getDislikeCount()).isEqualTo(post.getDislikeCount()+1);
    }

    @Test
    @DisplayName("게시물의 싫어요를 1 감소시킨다.")
    public void 싫어요_감소() throws Exception {
        //given
        //when
        postRepository.decreaseDislikeCount(post.getId());
        Optional<Post> result = postRepository.findById(post.getId());
        //then
        assertThat(result.get().getDislikeCount()).isEqualTo(post.getDislikeCount()-1);
    }
}