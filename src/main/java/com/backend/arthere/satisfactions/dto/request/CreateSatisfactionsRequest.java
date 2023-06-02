package com.backend.arthere.satisfactions.dto.request;

import com.backend.arthere.satisfactions.domain.SatisfactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateSatisfactionsRequest {

    @Min(value = 1, message = "id는 1이상의 수 입니다.")
    private Long artsId;

    @Min(value = 0, message = "score는 0이상의 수 입니다.")
    @Max(value = 5, message = "score는 5이하의 수 입니다.")
    private Integer starRating;

    private List<String> satisfactions;

    public List<SatisfactionType> getSatisfactionsType() {

        List<SatisfactionType> satisfactionType = new ArrayList<>();

        for (String satisfaction : satisfactions) {
            satisfactionType.add(SatisfactionType.valueOfName(satisfaction));
        }
        return satisfactionType;
    }
}
