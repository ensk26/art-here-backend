package com.backend.arthere.dislike.domain;

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
import static com.backend.arthere.fixture.MemberFixtures.회원_아이디;
import static com.backend.arthere.fixture.PostFixtures.게시물;
import static com.backend.arthere.fixture.PostFixtures.게시물_아이디;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@Import({QueryDslConfig.class, JpaConfig.class})
class DislikeRepositoryTest {

    @Autowired
    private DislikeRepository dislikeRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원, 게시물로 싫어요를 조회한다.")
    public void 싫어요_조회() throws Exception {
        //given
        Member member = 회원();
        memberRepository.save(member);

        Post post = 게시물(null, member);
        postRepository.save(post);
        Dislike dislike = new Dislike(member, post);
        dislikeRepository.save(dislike);

        //when
        Optional<Dislike> findDislike = dislikeRepository.findByMemberAndPost(member, post);
        //then
        assertAll(
                () -> assertThat(findDislike.get().getId()).isEqualTo(dislike.getId()),
                () -> assertThat(findDislike.get().getPost().getId()).isEqualTo(dislike.getPost().getId())
        );
    }

    @Test
    @DisplayName("회원, 게시물로 싫어요를 삭제한다.")
    public void 싫어요_삭제() throws Exception {
        //given
        Member member = 회원();
        memberRepository.save(member);

        Post post = 게시물(null, member);
        postRepository.save(post);
        Dislike dislike = new Dislike(member, post);
        dislikeRepository.save(dislike);
        //when
        dislikeRepository.deleteByMemberIdAndPostId(member.getId(), post.getId());
        //then
        assertThat(dislikeRepository.existsById(dislike.getId())).isFalse();
    }

    @Test
    @DisplayName("이전에 싫어요를 누른 적이 있다면 싫어요 정보가 존재한다.")
    public void 싫어요가_존재할_때_true_반환() throws Exception {
        //given
        Member member = 회원();
        memberRepository.save(member);

        Post post = 게시물(null, member);
        postRepository.save(post);
        Dislike dislike = new Dislike(member, post);
        dislikeRepository.save(dislike);
        //when
        boolean exists = dislikeRepository.existsByMemberIdAndPostId(member.getId(), post.getId());
        //then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("이전에 싫어요를 누른 적이 없다면 싫어요 정보가 존재하지 않는다.")
    public void 싫어요가_존재하지_않을_때_false_반환() throws Exception {
        //given
        //when
        boolean exists = dislikeRepository.existsByMemberIdAndPostId(회원_아이디, 게시물_아이디);
        //then
        assertThat(exists).isFalse();
    }
}