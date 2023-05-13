package com.backend.arthere.satisfactions.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SatisfactionsListResponse {

    List<SatisfactionsCountResponse> satisfactionsCount;

    Long starRating;

    Long postCount;

    public SatisfactionsListResponse(Long starRating, Long postCount) {
        this.starRating = starRating;
        this.postCount = postCount;
    }

    public void setSatisfactionsCount(List<SatisfactionsCountResponse> satisfactionsCount) {
        this.satisfactionsCount = satisfactionsCount;
    }
}
