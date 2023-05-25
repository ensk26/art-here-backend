package com.backend.arthere.starRatings.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.backend.arthere.satisfactions.domain.QSatisfactions.satisfactions;
import static com.backend.arthere.satisfactions.domain.QStarRatings.starRatings;

@Repository
@RequiredArgsConstructor
public class StarRatingsCustomRepositoryImpl implements StarRatingsCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Integer findStarRatings(Long artId, Long userId) {

        return jpaQueryFactory.select(starRatings.starRating.coalesce(0).as("starRating"))
                .from(starRatings)
                .where(starRatings.arts.id.eq(artId), satisfactions.member.id.eq(userId))
                .fetchOne();
    }
}
