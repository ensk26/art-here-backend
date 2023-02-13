package com.backend.arthere.arts.application;

import com.backend.arthere.arts.domain.ArtsRepository;
import com.backend.arthere.arts.dto.ArtImageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtsService {

    private final ArtsRepository artsRepository;

    public List<ArtImageResponse> findArtImageByRevisionDate(Long offset, Long limit) {

        return artsRepository.findArtImageByRevisionDate(offset, limit);
    }
}