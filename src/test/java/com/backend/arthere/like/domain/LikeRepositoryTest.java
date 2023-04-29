package com.backend.arthere.like.domain;

import com.backend.arthere.global.config.JpaConfig;
import com.backend.arthere.global.config.QueryDslConfig;
import com.backend.arthere.member.domain.Member;
import com.backend.arthere.member.domain.MemberRepository;
import com.backend.arthere.post.domain.Post;
import com.backend.arthere.post.domain.PostRepository;
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
class LikeRepositoryTest {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Test
    @DisplayName("회원, 게시물로 좋아요를 조회한다.")
    public void 좋아요_조회() throws Exception {
        //given
        Member member = 회원();
        memberRepository.save(member);

        Post post = 게시물(null, member);
        postRepository.save(post);
        Like like = new Like(member, post);
        likeRepository.save(like);
        //when
        Optional<Like> findLike = likeRepository.findByMemberAndPost(member, post);
        //then
        assertAll(
                () -> assertThat(findLike.get().getId()).isEqualTo(like.getId()),
                () -> assertThat(findLike.get().getPost().getId()).isEqualTo(like.getPost().getId())
        );
    }

    @Test
    @DisplayName("이전에 좋아요를 누른 적이 있다면 좋아요 정보가 존재한다.")
    public void 좋아요_존재() throws Exception {
        //given
        Member member = 회원();
        memberRepository.save(member);

        Post post = 게시물(null, member);
        postRepository.save(post);
        Like like = new Like(member, post);
        likeRepository.save(like);
        //when
        boolean exists = likeRepository.existsByMemberIdAndPostId(member.getId(), post.getId());
        //then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("회원, 게시물로 좋아요를 삭제한다.")
    public void 좋아요_삭제() throws Exception {
        //given
        Member member = 회원();
        memberRepository.save(member);

        Post post = 게시물(null, member);
        postRepository.save(post);
        Like like = new Like(member, post);
        likeRepository.save(like);
        //when
        likeRepository.deleteByMemberIdAndPostId(member.getId(), post.getId());
        //then
        assertThat(likeRepository.existsById(like.getId())).isFalse();
    }

}