package com.backend.arthere.arts.application;

import com.backend.arthere.arts.domain.ArtsRepository;
import com.backend.arthere.arts.dto.*;
import com.backend.arthere.arts.exception.ArtsNotFoundException;
import com.backend.arthere.arts.exception.QueryNotInputException;
import com.backend.arthere.arts.util.LocationUtils;
import com.backend.arthere.image.util.PresignedURLUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArtsService {

    private final ArtsRepository artsRepository;

    private final PresignedURLUtils presignedURLUtils;

    private final LocationUtils locationUtils;

    public ArtImageByRevisionDateResponse findArtImageByRevisionDate(ArtImageByRevisionDateRequest request) {

        Long id = null;
        LocalDateTime next = null;
        List<ArtImageResponse> artImageResponses = artsRepository.findArtImageByRevisionDate(request);

        if (artImageResponses.isEmpty()) {
            throw new ArtsNotFoundException();
        }

        Boolean hasNext = hasNext(artImageResponses, request.getLimit() + 1);
        if (hasNext) {
            id = artImageResponses.get(artImageResponses.size() - 1).getId();
            next = artsRepository.findRevisionDateById(id).get(0);
        }

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

    public ArtImageByAddressResponse searchArtImageByAddress(final ArtImageByAddressRequest request) {

        if (request.getQuery() == null || request.getQuery().isEmpty()) {
            throw new QueryNotInputException();
        }
        List<ArtImageResponse> artImageResponses = artsRepository.findArtImageByAddress(request);

        Boolean hasNext = hasNext(artImageResponses, request.getLimit() + 1);
        Long nextIdx = null;
        if (hasNext) {
            nextIdx = artImageResponses.get(artImageResponses.size() - 1).getId();
        }
        createImageSharePresignedURLByImageURL(artImageResponses);
        return new ArtImageByAddressResponse(artImageResponses, hasNext, nextIdx);

    }

    public ArtImageByArtNameResponse searchArtImageByArtName(ArtImageByArtNameRequest request) {

        Long nextId = null;
        List<ArtImageResponse> artImageResponses = artsRepository.findArtImageByArtName(request);

        if (artImageResponses.isEmpty()) {
            throw new ArtsNotFoundException();
        }

        Boolean hasNext = hasNext(artImageResponses, request.getLimit() + 1);
        if (hasNext) {
            nextId = artImageResponses.get(artImageResponses.size() - 1).getId();
        }

        createImageSharePresignedURLByImageURL(artImageResponses);

        return new ArtImageByArtNameResponse(artImageResponses, nextId, hasNext);
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