package com.backend.arthere.satisfactions.domain;

import com.backend.arthere.satisfactions.dto.response.SatisfactionsCountResponse;
import com.backend.arthere.satisfactions.dto.response.SatisfactionsListResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.backend.arthere.details.domain.QDetails.details;
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
    public SatisfactionsListResponse getTotalToDetailsById(Long id) {

        return jpaQueryFactory.select(Projections.constructor
                        (SatisfactionsListResponse.class, details.postTotal, details.satisfaction))
                .from(details)
                .where(details.arts.id.eq(id))
                .fetchOne();
    }
}
