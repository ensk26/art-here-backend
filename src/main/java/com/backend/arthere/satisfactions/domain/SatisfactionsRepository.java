package com.backend.arthere.satisfactions.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SatisfactionsRepository extends JpaRepository<Satisfactions, Long>, SatisfactionsCustomRepository {

}
