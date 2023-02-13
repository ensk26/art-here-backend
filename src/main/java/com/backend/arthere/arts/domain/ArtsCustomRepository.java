package com.backend.arthere.arts.domain;

import com.backend.arthere.arts.dto.ArtImageResponse;

import java.util.List;

public interface ArtsCustomRepository {

    List<ArtImageResponse> findArtImageByRevisionDate(Long offset, Long limit);
}
