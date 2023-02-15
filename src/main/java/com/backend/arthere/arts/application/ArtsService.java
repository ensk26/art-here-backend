package com.backend.arthere.arts.application;

import com.backend.arthere.arts.domain.ArtsRepository;
import com.backend.arthere.arts.dto.ArtImageByLocationResponse;
import com.backend.arthere.arts.dto.ArtImageResponse;
import com.backend.arthere.arts.dto.LocationRangeResponse;
import com.backend.arthere.arts.exception.ArtsNotFoundException;
import com.backend.arthere.arts.util.LocationUtils;
import com.backend.arthere.image.util.PresignedURLUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtsService {

    private final ArtsRepository artsRepository;

    private final PresignedURLUtils presignedURLUtils;

    private final LocationUtils locationUtils;

    public List<ArtImageResponse> findArtImageByRevisionDate(Long offset, Long limit) {

        List<ArtImageResponse> artImageResponses = artsRepository.findArtImageByRevisionDate(offset, limit);

        if (artImageResponses.isEmpty()) {
            throw new ArtsNotFoundException();
        }

        createImageSharePresignedURLByImageURL(artImageResponses);

        return artImageResponses;
    }

    public List<ArtImageByLocationResponse> findArtImageByLocation(Double latitude, Double longitude) {

        LocationRangeResponse locationRangeResponse = locationUtils.getLocationRange(latitude, longitude);

        List<ArtImageByLocationResponse> artImageResponses = artsRepository.findArtImageByLocation(locationRangeResponse);
        locationUtils.removeIncorrectLocation(latitude, longitude, artImageResponses);

        if (artImageResponses.isEmpty()) {
            throw new ArtsNotFoundException();
        }

        createImageBylocationSharePresignedURLByImageURL(artImageResponses);

        return artImageResponses;
    }

    private void createImageSharePresignedURLByImageURL(List<ArtImageResponse> artImageResponses) {

        for (ArtImageResponse artImageRespons : artImageResponses) {

            String presignedURL = presignedURLUtils.createImageShareURL(artImageRespons.getImageURL());
            artImageRespons.setImageURL(presignedURL);
        }
    }

    private void createImageBylocationSharePresignedURLByImageURL(List<ArtImageByLocationResponse> artImageResponses) {

        for (ArtImageByLocationResponse artImageRespons : artImageResponses) {

            String presignedURL = presignedURLUtils.createImageShareURL(artImageRespons.getImageURL());
            artImageRespons.setImageURL(presignedURL);
        }
    }
}