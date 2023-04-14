package com.backend.arthere.arts.domain;

import com.backend.arthere.arts.dto.request.ArtImageByAddressRequest;
import com.backend.arthere.arts.dto.request.ArtImageByArtNameRequest;
import com.backend.arthere.arts.dto.request.ArtImageByRevisionDateRequest;
import com.backend.arthere.arts.dto.response.ArtImageByLocationResponse;
import com.backend.arthere.arts.dto.response.ArtImageResponse;
import com.backend.arthere.arts.dto.response.LocationRangeResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface ArtsCustomRepository {

    List<ArtImageResponse> findArtImageByRevisionDate(ArtImageByRevisionDateRequest request);

    List<ArtImageByLocationResponse> findArtImageByLocation(LocationRangeResponse locationRangeResponse);

    List<LocalDateTime> findRevisionDateById(Long id);

    List<ArtImageResponse> findArtImageByAddress(ArtImageByAddressRequest request);

    List<ArtImageResponse> findArtImageByArtName(ArtImageByArtNameRequest request);
}
