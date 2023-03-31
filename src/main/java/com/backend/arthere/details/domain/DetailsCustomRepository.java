package com.backend.arthere.details.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DetailsCustomRepository {

    Page<Details> findDetailsWithArts(Pageable pageable);
}
