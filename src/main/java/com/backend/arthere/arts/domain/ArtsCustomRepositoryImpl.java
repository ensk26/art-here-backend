package com.backend.arthere.arts.domain;

import com.backend.arthere.arts.dto.ArtImageResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.backend.arthere.arts.domain.QArts.arts;

@Repository
@RequiredArgsConstructor
public class ArtsCustomRepositoryImpl implements ArtsCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ArtImageResponse> findArtImageByRevisionDate(Long offset, Long limit) {
        return jpaQueryFactory.select(Projections.constructor(ArtImageResponse.class, arts.id, arts.artName, arts.imageURL))
                .from(arts)
                .orderBy(arts.revisionDate.desc())
                .offset(offset)
                .limit(limit)
                .fetch();
    }
}
