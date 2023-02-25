package com.backend.arthere.arts.domain;

import com.backend.arthere.arts.dto.ArtImageByLocationResponse;
import com.backend.arthere.arts.dto.ArtImageResponse;
import com.backend.arthere.arts.dto.LocationRangeResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface ArtsCustomRepository {

    List<ArtImageResponse> findArtImageByRevisionDate(LocalDateTime revisionDateIdx, Integer limit);

    List<ArtImageByLocationResponse> findArtImageByLocation(LocationRangeResponse locationRangeResponse);

    String findRevisionDateById(Long id);

}
