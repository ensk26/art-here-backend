package com.backend.arthere.arts.domain;

import com.backend.arthere.arts.dto.request.ArtImageByAddressRequest;
import com.backend.arthere.arts.dto.request.ArtImageByArtNameRequest;
import com.backend.arthere.arts.dto.request.ArtImageByRevisionDateRequest;
import com.backend.arthere.arts.dto.response.ArtImageByLocationResponse;
import com.backend.arthere.arts.dto.response.ArtImageResponse;
import com.backend.arthere.arts.dto.response.LocationRangeResponse;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

import static com.backend.arthere.arts.domain.QArts.arts;

@Repository
@RequiredArgsConstructor
public class ArtsCustomRepositoryImpl implements ArtsCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ArtImageResponse> findArtImageByRevisionDate(ArtImageByRevisionDateRequest request) {

        List<ArtImageResponse> revisionDate = findRevisionDateEqual(request);

        if (revisionDate.size() < request.getLimit()) {
            int limit = request.getLimit() - revisionDate.size();
            revisionDate.addAll(findRevisionDateBefore(request, limit));
        }
        return revisionDate;
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
    public List<ArtImageResponse> findArtImageByAddress(final ArtImageByAddressRequest request) {

        return jpaQueryFactory.select(Projections.constructor
                        (ArtImageResponse.class, arts.id, arts.artName, arts.imageURL))
                .from(arts)
                .where(
                        ltArtsId(request.getIdx()),
                        containsAddress(request.getQuery()),
                        containCategory(request.getCategory())
                )
                .limit(request.getLimit() + 1)
                .orderBy(arts.id.desc())
                .fetch();

    }

    @Override
    public List<ArtImageResponse> findArtImageByArtName(ArtImageByArtNameRequest request) {

        return jpaQueryFactory.select(Projections
                        .constructor(ArtImageResponse.class, arts.id, arts.artName, arts.imageURL))
                .from(arts)
                .where(
                        ltArtsId(request.getIdx()),
                        containArtName(request.getName()),
                        containCategory(request.getCategory())
                )
                .orderBy(arts.id.desc())
                .limit(request.getLimit() + 1)
                .fetch();
    }

    private List<ArtImageResponse> findRevisionDateEqual(ArtImageByRevisionDateRequest request) {

        return jpaQueryFactory.select(Projections
                        .constructor(ArtImageResponse.class, arts.id, arts.artName, arts.imageURL))
                .from(arts)
                .orderBy(arts.revisionDate.desc(), arts.id.desc())
                .where(
                        revisionDateEqualIdx(request.getDate(), request.getIdx()),
                        containCategory(request.getCategory())
                )
                .limit(request.getLimit() + 1)
                .fetch();
    }

    private List<ArtImageResponse> findRevisionDateBefore(ArtImageByRevisionDateRequest request, int limit) {

        return jpaQueryFactory.select(Projections
                        .constructor(ArtImageResponse.class, arts.id, arts.artName, arts.imageURL))
                .from(arts)
                .orderBy(arts.revisionDate.desc(), arts.id.desc())
                .where(
                        revisionDateBeforeIdx(request.getDate(), request.getIdx()),
                        containCategory(request.getCategory())
                )
                .limit(limit + 1)
                .fetch();
    }

    private BooleanExpression containsAddress(final String query) {
        if (!StringUtils.hasText(query)) {
            return null;
        }
        return arts.address.roadAddress.contains(query);
    }

    private BooleanExpression containArtName(String artName) {
        if (!StringUtils.hasText(artName)) {
            return null;
        }
        return arts.artName.contains(artName);
    }

    private BooleanExpression ltArtsId(final Long idx) {
        if (idx == null) {
            return null;
        }
        return arts.id.lt(idx);
    }

    private BooleanExpression containCategory(final Category category) {
        if (category == null) {
            return null;
        }
        return arts.category.eq(category);
    }

    @Override
    public List<LocalDateTime> findRevisionDateById(Long id) {
        return jpaQueryFactory.select(arts.revisionDate)
                .from(arts)
                .where(arts.id.eq(id))
                .fetch();
    }

    private BooleanExpression revisionDateEqualIdx(LocalDateTime dateIdx, Long idx) {

        if (dateIdx == null || idx == null) {
            return null;
        }
        return arts.revisionDate.eq(dateIdx).and(arts.id.lt(idx));
    }

    private BooleanExpression revisionDateBeforeIdx(LocalDateTime dateIdx, Long idx) {

        if (dateIdx == null || idx == null) {
            return null;
        }
        return arts.revisionDate.lt(dateIdx);
    }
}
