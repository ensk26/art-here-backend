package com.backend.arthere.arts.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    PICTURE("사진"), MURAL("벽화"), CRAFT("공예"), SCULPTURE("조각"),
    CONVERSATION("회화"), CALLIGRAPHY("서예"), MEDIA("미디어"),
    OTHER("기타");

    private final String categoryName;
}
