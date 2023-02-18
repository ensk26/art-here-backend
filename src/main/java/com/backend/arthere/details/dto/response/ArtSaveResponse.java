package com.backend.arthere.details.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ArtSaveResponse {
    private Long id;

    public ArtSaveResponse(final Long id) {
        this.id = id;
    }
}
