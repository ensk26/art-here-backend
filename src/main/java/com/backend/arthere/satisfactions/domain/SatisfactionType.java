package com.backend.arthere.satisfactions.domain;

import com.backend.arthere.arts.exception.InvalidCategoryException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum SatisfactionType {

    COOL("멋져요"), GOOD_LOCATION("장소에 어울려요"), NATURE_FRIENDLY("자연힐링"),
    PHOTO_BOOTH("여기가포토존"), FUN("재밌어요"), DAMAGED("파손됐어요"),
    NOT_PRETTY("안예뻐요"), MESSY("지저분해요"), SUITABLE("장소랑안어울려요"),
    UNPLEASANT("불쾌해요");

    private final String satisfactionName;

    public static SatisfactionType valueOfName(String satisfactionName) {
        return Arrays.stream(values())
                .filter(value -> value.satisfactionName.equals(satisfactionName))
                .findAny()
                .orElseThrow(InvalidCategoryException::new);
    }
}
