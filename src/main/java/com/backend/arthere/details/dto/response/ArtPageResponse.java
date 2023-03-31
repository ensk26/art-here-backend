package com.backend.arthere.details.dto.response;

import lombok.Getter;


import java.util.List;

@Getter
public class ArtPageResponse {
    private Long totalElements;
    private int totalPages;
    private List<ArtForAdminResponse> artForAdminResponses;

    public ArtPageResponse(final Long totalElements, final int totalPages,
                           final List<ArtForAdminResponse> artForAdminResponses) {
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.artForAdminResponses = artForAdminResponses;
    }

}
