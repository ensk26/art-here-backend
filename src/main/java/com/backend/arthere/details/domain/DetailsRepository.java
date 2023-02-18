package com.backend.arthere.details.domain;

import com.backend.arthere.arts.domain.Arts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DetailsRepository  extends JpaRepository<Details, Long> {
    Optional<Details> findByArts(Arts arts);
}
