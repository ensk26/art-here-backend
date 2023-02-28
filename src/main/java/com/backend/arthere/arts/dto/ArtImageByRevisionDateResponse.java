package com.backend.arthere.arts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ArtImageByRevisionDateResponse {

    private List<ArtImageResponse> artImageResponses;

    private Long nextIdx;

    private LocalDateTime nextRevisionDateIdx;

    private Boolean hasNext;
}
