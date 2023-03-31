package com.backend.arthere.details.domain;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DetailsRepository  extends JpaRepository<Details, Long>, DetailsCustomRepository {

    @EntityGraph(attributePaths = {"arts"})
    Optional<Details> findByArtsId(Long artsId);

}
