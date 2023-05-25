package com.backend.arthere.satisfactions.domain;

import com.backend.arthere.satisfactions.dto.response.GetTotalToDetailsResponse;
import com.backend.arthere.satisfactions.dto.response.SatisfactionsCountResponse;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;

import static com.backend.arthere.arts.domain.QArts.arts;
import static com.backend.arthere.satisfactions.domain.QSatisfactions.satisfactions;

@Repository
@RequiredArgsConstructor
public class SatisfactionsCustomRepositoryImpl implements SatisfactionsCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public List<SatisfactionsCountResponse> findSatisfactionsCount(Long id) {

        List<SatisfactionType> types = new ArrayList<>(Arrays.asList(SatisfactionType.values()));

        List<Tuple> typeCount = jpaQueryFactory.select(satisfactions.satisfactionType, satisfactions.count())
                .from(satisfactions)
                .where(satisfactions.arts.id.eq(id))
                .groupBy(satisfactions.satisfactionType)
                .fetch();


        Map<SatisfactionType, Long> countMap = new HashMap<>();
        typeCount.forEach(tuple -> countMap.put(tuple.get(satisfactions.satisfactionType), tuple.get(satisfactions.count())));
        types.forEach(type -> countMap.putIfAbsent(type, 0L));

        List<SatisfactionsCountResponse> results = new ArrayList<>();
        for (Map.Entry<SatisfactionType, Long> value : countMap.entrySet()) {
            results.add(new SatisfactionsCountResponse(value.getKey(), value.getValue()));
        }

        return results;
    }

    @Override
    public GetTotalToDetailsResponse getTotalToDetailsById(Long id) {

        return jpaQueryFactory.select(Projections.constructor
                        (GetTotalToDetailsResponse.class, arts.starRating, arts.postTotal))
                .from(arts)
                .where(arts.id.eq(id))
                .fetchOne();
    }

    @Override
    public List<SatisfactionType> findSatisfactionsType(Long artId, Long userId) {

        return jpaQueryFactory.select(satisfactions.satisfactionType)
                .from(satisfactions)
                .where(satisfactions.arts.id.eq(artId), satisfactions.member.id.eq(userId))
                .fetch();
    }

}
