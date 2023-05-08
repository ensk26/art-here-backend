package com.backend.arthere.satisfactions.domain;

import com.backend.arthere.satisfactions.dto.response.SatisfactionsListResponse;

import java.util.List;

public interface SatisfactionsCustomRepository {

    List<SatisfactionsListResponse> findSatisfactionsList(Long id);
}
