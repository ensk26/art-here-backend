package com.backend.arthere.post.domain;

import com.backend.arthere.post.dto.response.PostInfoResponse;
import com.backend.arthere.post.dto.response.PostsResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.backend.arthere.post.domain.QPost.post;

@Repository
@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public PostsResponse findPostsByArtsId(Long artsId, String sorting, String cursor) {

        List<PostInfoResponse> postInfos = jpaQueryFactory.select(Projections.constructor(
                        PostInfoResponse.class, post.id, post.title, post.member.name, post.imageURL, post.likeCount))
                .from(post)
                .where(post.arts.id.eq(artsId), cursorId(sorting, cursor))
                .limit(9)
                .fetch();

        if (postInfos.size() < 9) {
            return new PostsResponse(postInfos, null);
        }

        return new PostsResponse(postInfos, generateCursor(sorting, postInfos.get(8).getId(), postInfos.get(8).getLike()));
    }

    private BooleanExpression cursorId(String sorting, String cursor) {
        if (cursor == null) {
            return null;
        }
        if ("like".equals(sorting)) {
            return StringExpressions
                    .lpad(post.id.stringValue(), 8, '0')
                    .concat(StringExpressions
                            .lpad(post.likeCount.stringValue(), 8, '0'))
                    .gt(cursor);
        } else {
            return StringExpressions
                    .lpad(post.id.stringValue(), 8, '0')
                    .gt(cursor);
        }
    }

    String generateCursor(String sorting, Long id, Long likeCount) {
        if ("like".equals(sorting)) {
            return String.format("%08d", id) + String.format("%08d", likeCount);
        } else {
            return String.format("%08d", id);
        }
    }
}
