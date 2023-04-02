package com.backend.arthere.arts.domain;

import com.backend.arthere.arts.exception.InvalidCategoryException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Category {
    PICTURE("사진"), MURAL("벽화"), CRAFT("공예"), SCULPTURE("조각"),
    PAINTING("그림"), INSTALLATION("설치미술"), KINETIC("키네틱아트"),
    RESPONSIVE("반응형 미디어"), LIGHTING("조명예술"), OTHER("기타");

    private final String categoryName;

    public static Category valueOfName(String categoryName) {
        return Arrays.stream(values())
                .filter(value -> value.categoryName.equals(categoryName))
                .findAny()
                .orElseThrow(InvalidCategoryException::new);
    }
}
