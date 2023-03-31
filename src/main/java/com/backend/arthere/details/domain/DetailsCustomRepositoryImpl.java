package com.backend.arthere.details.domain;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.backend.arthere.details.domain.QDetails.details;


@Repository
@RequiredArgsConstructor
public class DetailsCustomRepositoryImpl implements DetailsCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Details> findDetailsWithArts(final Pageable pageable) {
        List<Details> contents = jpaQueryFactory.select(details)
                .from(details)
                .join(details.arts)
                .fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(details.id.desc())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(details.count())
                .from(details);

        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
    }
}
