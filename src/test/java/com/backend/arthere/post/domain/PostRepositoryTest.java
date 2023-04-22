package com.backend.arthere.post.domain;

import com.backend.arthere.arts.domain.Arts;
import com.backend.arthere.arts.domain.ArtsRepository;
import com.backend.arthere.global.config.JpaConfig;
import com.backend.arthere.global.config.QueryDslConfig;
import com.backend.arthere.member.domain.Member;
import com.backend.arthere.member.domain.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static com.backend.arthere.fixture.ArtsFixtures.작품;
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

    @Autowired
    private ArtsRepository artsRepository;

    @Test
    @DisplayName("게시물을 조회한다.")
    public void 게시물_조회() throws Exception {
        //given
        Member member = 회원();
        memberRepository.save(member);

        Arts arts = 작품(null);
        artsRepository.save(arts);

        Post post = 게시물(member, arts);
        postRepository.save(post);

        //when
        Optional<Post> result = postRepository.findPostWithMember(post.getId());

        //then
        assertAll(
                () -> assertThat(result.get().getId()).isEqualTo(post.getId()),
                () -> assertThat(result.get().getTitle()).isEqualTo(post.getTitle()),
                () -> assertThat(result.get().getMember().getId()).isEqualTo(member.getId())
        );
    }
}