package com.backend.arthere.arts.domain;

import com.backend.arthere.arts.dto.ArtImageByLocationResponse;
import com.backend.arthere.arts.dto.ArtImageResponse;
import com.backend.arthere.arts.dto.LocationRangeResponse;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.backend.arthere.arts.domain.QArts.arts;

@Repository
@RequiredArgsConstructor
public class ArtsCustomRepositoryImpl implements ArtsCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ArtImageResponse> findArtImageByRevisionDate(LocalDateTime revisionDateIdx, Integer limit) {
        return jpaQueryFactory.select(Projections.constructor(ArtImageResponse.class, arts.id, arts.artName, arts.imageURL))
                .from(arts)
                .where(revisionDateIdx(revisionDateIdx))
                .orderBy(arts.id.desc())
                .limit(limit + 1)
                .fetch();
    }

    @Override
    public List<ArtImageByLocationResponse> findArtImageByLocation(LocationRangeResponse locationRangeResponse) {

        BooleanBuilder builder = new BooleanBuilder();
        if (locationRangeResponse.getMaxLatitude() != null) {
            builder.and(arts.location.latitude.loe(locationRangeResponse.getMaxLatitude()));
        }
        if (locationRangeResponse.getMinLatitude() != null) {
            builder.and(arts.location.latitude.goe(locationRangeResponse.getMinLatitude()));
        }
        if (locationRangeResponse.getMaxLongitude() != null) {
            builder.and(arts.location.longitude.loe(locationRangeResponse.getMaxLongitude()));
        }
        if (locationRangeResponse.getMinLongitude() != null) {
            builder.and(arts.location.longitude.goe(locationRangeResponse.getMinLongitude()));
        }

        return jpaQueryFactory.select(Projections.constructor(ArtImageByLocationResponse.class,
                        arts.id, arts.artName, arts.imageURL, arts.location.latitude, arts.location.longitude))
                .from(arts)
                .orderBy(arts.location.latitude.asc())
                .where(builder)
                .fetch();
    }

    @Override
    public String findRevisionDateById(Long id) {
        return jpaQueryFactory.select(arts.revisionDate)
                .from(arts)
                .where(arts.id.eq(id))
                .fetch()
                .toString();
    }

    private BooleanExpression revisionDateIdx(LocalDateTime lastIdx) {
        if (lastIdx == null) {
            return null;
        }

        return arts.revisionDate.lt(lastIdx);
    }
}
