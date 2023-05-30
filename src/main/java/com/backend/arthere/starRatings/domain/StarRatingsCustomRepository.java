package com.backend.arthere.starRatings.domain;

public interface StarRatingsCustomRepository {

    Integer findStarRatings(Long artId, Long userId);
}
