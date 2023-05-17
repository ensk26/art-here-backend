package com.backend.arthere.satisfactions.dto.response;

import com.backend.arthere.satisfactions.domain.SatisfactionType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SatisfactionsCountResponse {

    private String satisfaction;

    private Long count;

    public SatisfactionsCountResponse(SatisfactionType satisfaction, Long count) {
        this.satisfaction = satisfaction.getSatisfactionName();
        this.count = count;
    }
}
