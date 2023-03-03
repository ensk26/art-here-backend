package com.backend.arthere.arts.domain;

import com.backend.arthere.arts.dto.*;

import java.time.LocalDateTime;
import java.util.List;

public interface ArtsCustomRepository {

    List<ArtImageResponse> findArtImageByRevisionDate(ArtImageByRevisionDateRequest request);

    List<ArtImageByLocationResponse> findArtImageByLocation(LocationRangeResponse locationRangeResponse);

    List<LocalDateTime> findRevisionDateById(Long id);


    List<ArtImageResponse> findArtImageByAddress(ArtImageByAddressRequest request);
}
