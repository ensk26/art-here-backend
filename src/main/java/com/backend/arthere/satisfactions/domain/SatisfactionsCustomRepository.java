package com.backend.arthere.satisfactions.domain;

import com.backend.arthere.satisfactions.dto.response.SatisfactionsResponse;

import java.util.List;

public interface SatisfactionsCustomRepository {

    List<SatisfactionsResponse> findSatisfactions(Long id);
}
