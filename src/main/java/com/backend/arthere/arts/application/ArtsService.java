package com.backend.arthere.arts.application;

import com.backend.arthere.arts.domain.ArtsRepository;
import com.backend.arthere.arts.dto.*;
import com.backend.arthere.arts.exception.ArtsNotFoundException;
import com.backend.arthere.arts.util.LocationUtils;
import com.backend.arthere.image.util.PresignedURLUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtsService {

    private final ArtsRepository artsRepository;

    private final PresignedURLUtils presignedURLUtils;

    private final LocationUtils locationUtils;

    public ArtImageByRevisionDateResponse findArtImageByRevisionDate(ArtImageByRevisionDateRequest request) {

        List<ArtImageResponse> artImageResponses = artsRepository.findArtImageByRevisionDate(request);

        if (artImageResponses.isEmpty()) {
            throw new ArtsNotFoundException();
        }

        Boolean hasNext = hasNext(artImageResponses, request.getLimit() + 1);

        Long id = artImageResponses.get(artImageResponses.size() - 1).getId();
        LocalDateTime next = artsRepository.findRevisionDateById(id).get(0);

        createImageSharePresignedURLByImageURL(artImageResponses);

        return new ArtImageByRevisionDateResponse(artImageResponses, id, next, hasNext);
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

    private Boolean hasNext(List<ArtImageResponse> artImageResponses, int size) {
        if (artImageResponses.size() == size) {
            artImageResponses.remove(size - 1);
            return true;
        }
        return false;
    }
}