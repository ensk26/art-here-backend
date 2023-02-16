package com.backend.arthere.arts.domain;

import com.backend.arthere.arts.dto.ArtImageByLocationResponse;
import com.backend.arthere.arts.dto.ArtImageResponse;
import com.backend.arthere.arts.dto.LocationRangeResponse;

import java.util.List;

public interface ArtsCustomRepository {

    List<ArtImageResponse> findArtImageByRevisionDate(Long offset, Long limit);

    List<ArtImageByLocationResponse> findArtImageByLocation(LocationRangeResponse locationRangeResponse);
}
