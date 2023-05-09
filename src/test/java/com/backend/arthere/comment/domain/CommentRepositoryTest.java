package com.backend.arthere.comment.domain;

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

import static com.backend.arthere.fixture.CommentFixtures.댓글;
import static com.backend.arthere.fixture.MemberFixtures.회원;
import static com.backend.arthere.fixture.PostFixtures.게시물;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({QueryDslConfig.class, JpaConfig.class})
class CommentRepositoryTest {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    @DisplayName("댓글을 저장한다.")
    public void 댓글_저장() throws Exception {
        //given
        Member member = 회원();
        memberRepository.save(member);

        Post post = 게시물(null, member);
        postRepository.save(post);
        Comment comment = 댓글(null, member, post);
        //when
        Comment saveComment = commentRepository.save(comment);
        //then
        assertThat(comment).isSameAs(saveComment);

    }
}