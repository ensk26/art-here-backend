package com.backend.arthere.fixture;

import com.backend.arthere.arts.domain.Arts;
import com.backend.arthere.member.domain.Member;
import com.backend.arthere.post.domain.Post;
import com.backend.arthere.post.dto.response.PostResponse;

import java.time.LocalDateTime;

import static com.backend.arthere.fixture.MemberFixtures.회원;

public class PostFixtures {

    public static final Long 게시물_아이디 = 1L;

    public static final String 게시물_제목 = "제목";

    public static final String 게시물_내용 = "내용";

    public static final String 게시물_사진 = "사진";

    public static final Long 좋아요_수 = 2L;

    public static final Long 싫어요_수 = 2L;

    public static Post 게시물(final Member member, final Arts arts) {
        return Post.builder()
                .title(게시물_제목)
                .content(게시물_내용)
                .imageURL(게시물_사진)
                .likeCount(좋아요_수)
                .dislikeCount(싫어요_수)
                .member(member).arts(arts).build();
    }

    public static Post 게시물(final Long id, final Member member) {
        return Post.builder()
                .id(id)
                .title(게시물_제목)
                .content(게시물_내용)
                .imageURL(게시물_사진)
                .likeCount(좋아요_수)
                .dislikeCount(싫어요_수)
                .member(member).build();
    }

    public static PostResponse 게시물_응답() {
        PostResponse postResponse = PostResponse.of(게시물(게시물_아이디, 회원(1L)), false, false);
        postResponse.setDate(LocalDateTime.now(), LocalDateTime.now());
        return postResponse;
    }

}
