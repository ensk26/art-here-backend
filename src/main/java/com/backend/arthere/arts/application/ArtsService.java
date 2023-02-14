package com.backend.arthere.arts.application;

import com.backend.arthere.arts.domain.ArtsRepository;
import com.backend.arthere.arts.dto.ArtImageResponse;
import com.backend.arthere.image.util.PresignedURLUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtsService {

    private final ArtsRepository artsRepository;

    private final PresignedURLUtils presignedURLUtils;

    public List<ArtImageResponse> findArtImageByRevisionDate(Long offset, Long limit) {

        List<ArtImageResponse> artImageResponses = artsRepository.findArtImageByRevisionDate(offset, limit);
        createImageSharePresignedURLByImageURL(artImageResponses);

        return artImageResponses;
    }

    private void createImageSharePresignedURLByImageURL(List<ArtImageResponse> artImageResponses) {

        for (ArtImageResponse artImageRespons : artImageResponses) {

            String presignedURL = presignedURLUtils.createImageShareURL(artImageRespons.getImageURL());
            artImageRespons.setImageURL(presignedURL);
        }
    }
}