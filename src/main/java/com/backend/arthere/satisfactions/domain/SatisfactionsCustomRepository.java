package com.backend.arthere.satisfactions.domain;

import com.backend.arthere.satisfactions.dto.response.SatisfactionsCountResponse;
import com.backend.arthere.satisfactions.dto.response.SatisfactionsListResponse;

import java.util.List;

public interface SatisfactionsCustomRepository {

    List<SatisfactionsCountResponse> findSatisfactionsCount(Long id);

    SatisfactionsListResponse getTotalToDetailsById(Long id);
}
