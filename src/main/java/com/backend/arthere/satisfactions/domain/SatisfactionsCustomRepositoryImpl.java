package com.backend.arthere.satisfactions.domain;

import com.backend.arthere.satisfactions.dto.response.SatisfactionsResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.backend.arthere.satisfactions.domain.QSatisfactions.satisfactions;

@Repository
@RequiredArgsConstructor
public class SatisfactionsCustomRepositoryImpl implements SatisfactionsCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<SatisfactionsResponse> findSatisfactions(Long id) {

        return jpaQueryFactory.select(Projections.constructor
                (SatisfactionsResponse.class, satisfactions.satisfactionType, satisfactions.count()))
                .from(satisfactions)
                .groupBy(satisfactions.satisfactionType)
                .fetch();
    }
}
