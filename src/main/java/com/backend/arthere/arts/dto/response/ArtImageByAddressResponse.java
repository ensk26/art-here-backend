package com.backend.arthere.arts.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ArtImageByAddressResponse {

    private List<ArtImageResponse> artImageResponses;
    private Boolean hasNext;
    private Long nextIdx;
}
