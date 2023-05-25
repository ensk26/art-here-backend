package com.backend.arthere.satisfactions.dto.request;

import com.backend.arthere.satisfactions.domain.SatisfactionType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class CreateSatisfactionsRequest {

    @Min(value = 1, message = "id는 1이상의 수 입니다.")
    private Long artsId;

    @Min(value = 1, message = "score는 1이상의 수 입니다.")
    @Max(value = 5, message = "score는 5이하의 수 입니다.")
    private Integer score;

    private List<SatisfactionType> satisfactions;

    private Integer starRating;

    public CreateSatisfactionsRequest(Long artsId, Integer score, List<String> satisfactions, Integer starRating) {
        this.artsId = artsId;
        this.score = score;
        this.satisfactions = toSatisfactionsType(satisfactions);
        this.starRating = starRating;
    }

    private List<SatisfactionType> toSatisfactionsType(List<String> satisfactions) {

        List<SatisfactionType> satisfactionType = new ArrayList<>();

        for (String satisfaction : satisfactions) {
            satisfactionType.add(SatisfactionType.valueOfName(satisfaction));
        }
        return satisfactionType;
    }
}
