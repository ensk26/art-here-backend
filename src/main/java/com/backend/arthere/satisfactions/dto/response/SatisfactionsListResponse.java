package com.backend.arthere.satisfactions.dto.response;

import com.backend.arthere.satisfactions.domain.SatisfactionType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SatisfactionsListResponse {

    private Long artsId;

    private String satisfaction;

    private Long count;

    public SatisfactionsListResponse(Long artsId, SatisfactionType satisfaction, Long count) {
        this.artsId = artsId;
        this.satisfaction = satisfaction.getSatisfactionName();
        this.count = count;
    }
}
