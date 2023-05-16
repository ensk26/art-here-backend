package com.backend.arthere.satisfactions.domain;

import com.backend.arthere.satisfactions.dto.response.GetTotalToDetailsResponse;
import com.backend.arthere.satisfactions.dto.response.SatisfactionsCountResponse;

import java.util.List;

public interface SatisfactionsCustomRepository {

    List<SatisfactionsCountResponse> findSatisfactionsCount(Long id);

    GetTotalToDetailsResponse getTotalToDetailsById(Long id);
}
