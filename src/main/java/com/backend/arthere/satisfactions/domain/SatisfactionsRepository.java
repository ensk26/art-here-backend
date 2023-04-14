package com.backend.arthere.satisfactions.domain;

import com.backend.arthere.arts.domain.Arts;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SatisfactionsRepository extends JpaRepository<Satisfactions, Long>, SatisfactionsCustomRepository {

}
