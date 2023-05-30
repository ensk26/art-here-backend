package com.backend.arthere.satisfactions.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetTotalToDetailsResponse {

    private String starRating;

    private Long postCount;

    public GetTotalToDetailsResponse(Long starRating, Long starRatingCount, Long postCount) {
        this.starRating = String.format("%.2f", (double) starRating / starRatingCount);
        this.postCount = postCount;
    }
}
