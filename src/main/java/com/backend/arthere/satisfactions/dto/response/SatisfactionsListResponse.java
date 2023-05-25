package com.backend.arthere.satisfactions.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SatisfactionsListResponse {

    List<SatisfactionsCountResponse> satisfactionsCount;

    String starRating;

    Long postCount;

    public SatisfactionsListResponse(List<SatisfactionsCountResponse> satisfactionsCount, String starRating, Long postCount) {
        this.satisfactionsCount = satisfactionsCount;
        this.starRating = starRating;
        this.postCount = postCount;
    }
}
