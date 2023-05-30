package com.backend.arthere.satisfactions.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SatisfactionsResponse {

    private List<String> satisfactions;

    private Integer starRating;

    public SatisfactionsResponse(List<String> satisfactions, Integer starRating) {
        this.satisfactions = satisfactions;
        this.starRating = starRating;
    }
}
