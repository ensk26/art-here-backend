package com.backend.arthere.starRatings.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StarRatingsRepository extends JpaRepository<StarRatings, Long>, StarRatingsCustomRepository {
}
