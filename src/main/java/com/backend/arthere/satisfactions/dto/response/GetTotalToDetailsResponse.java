package com.backend.arthere.satisfactions.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetTotalToDetailsResponse {

    Integer starRating;

    Long postCount;

    public GetTotalToDetailsResponse(Integer starRating, Long postCount) {
        this.starRating = starRating;
        this.postCount = postCount;
    }
}
