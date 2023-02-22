package com.backend.arthere.details.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ArtSaveResponse {
    private Long id;
    private String artName;

    public ArtSaveResponse(final Long id, final String artName) {
        this.id = id;
        this.artName = artName;
    }
}
