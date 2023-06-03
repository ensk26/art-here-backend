package com.backend.arthere.starRatings.domain;

public interface StarRatingsCustomRepository {

    Integer findStarRatingsId(Long artId, Long userId);

    StarRatings findStarRatings(Long artId, Long userId);
}
