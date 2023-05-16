package com.backend.arthere.satisfactions.domain;

import com.backend.arthere.satisfactions.dto.response.GetTotalToDetailsResponse;
import com.backend.arthere.satisfactions.dto.response.SatisfactionsCountResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.backend.arthere.arts.domain.QArts.arts;
import static com.backend.arthere.satisfactions.domain.QSatisfactions.satisfactions;

@Repository
@RequiredArgsConstructor
public class SatisfactionsCustomRepositoryImpl implements SatisfactionsCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<SatisfactionsCountResponse> findSatisfactionsCount(Long id) {

        return jpaQueryFactory.select(Projections.constructor
                        (SatisfactionsCountResponse.class, satisfactions.satisfactionType, satisfactions.count()))
                .from(satisfactions)
                .where(satisfactions.arts.id.eq(id))
                .groupBy(satisfactions.satisfactionType)
                .fetch();
    }

    @Override
    public GetTotalToDetailsResponse getTotalToDetailsById(Long id) {

        return jpaQueryFactory.select(Projections.constructor
                        (GetTotalToDetailsResponse.class, arts.postTotal, arts.starRating))
                .from(arts)
                .where(arts.id.eq(id))
                .fetchOne();
    }
}
