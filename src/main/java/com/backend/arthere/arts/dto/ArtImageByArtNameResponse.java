package com.backend.arthere.arts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ArtImageByArtNameResponse {

    private List<ArtImageResponse> artImageResponses;

    private Long nextIdx;

    private Boolean hasNext;
}
