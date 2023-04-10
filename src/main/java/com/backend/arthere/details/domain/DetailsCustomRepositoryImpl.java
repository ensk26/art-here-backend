package com.backend.arthere.details.domain;

import com.backend.arthere.arts.domain.Arts;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.backend.arthere.arts.domain.QArts.arts;
import static com.backend.arthere.details.domain.QDetails.details;


@Slf4j
@Repository
@RequiredArgsConstructor
public class DetailsCustomRepositoryImpl implements DetailsCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Details> findDetailsWithArts(final Pageable pageable) {
        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(details.count())
                .from(details);

        List<Long> artsId = jpaQueryFactory
                .select(arts.id)
                .from(arts)
                .orderBy(toOrderSpecifiers(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        if(artsId.isEmpty()) {
            return PageableExecutionUtils.getPage(Collections.emptyList(), pageable, countQuery::fetchOne);
        }

        List<Details> contents = jpaQueryFactory.select(details)
                .from(details)
                .join(details.arts, arts)
                .fetchJoin()
                .where(arts.id.in(artsId))
                .orderBy(toOrderSpecifiers(pageable))
                .fetch();

        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
    }

    private OrderSpecifier[] toOrderSpecifiers(final Pageable pageable) {
        return pageable.getSort()
                .stream()
                .map(sort -> getOrderSpecifier(sort))
                .collect(Collectors.toList()).toArray(OrderSpecifier[]::new);
    }

    private OrderSpecifier getOrderSpecifier (final Sort.Order order) {
        Order direction = order.isAscending() ? Order.ASC : Order.DESC;
        PathBuilder pathBuilder = new PathBuilder(Arts.class, "arts");
        return new OrderSpecifier(direction, pathBuilder.get(order.getProperty()));

    }

}
