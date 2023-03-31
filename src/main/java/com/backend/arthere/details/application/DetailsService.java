package com.backend.arthere.details.application;


import com.amazonaws.services.s3.AmazonS3;
import com.backend.arthere.details.domain.Details;
import com.backend.arthere.details.domain.DetailsRepository;
import com.backend.arthere.details.dto.request.ArtRequest;
import com.backend.arthere.details.dto.response.*;
import com.backend.arthere.arts.domain.Arts;
import com.backend.arthere.details.exception.DetailsNotFoundException;
import com.backend.arthere.image.util.PresignedURLUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DetailsService {
    private final DetailsRepository detailsRepository;
    private final PresignedURLUtils presignedURLUtils;

    private final AmazonS3 adminS3Client;

    private final String adminBucketName;

    @Transactional
    public ArtSaveResponse save(final ArtRequest artRequest) {
        Arts arts = artRequest.toArts();

        boolean state = getState(artRequest.getEndDate());
        Details details = artRequest.toDetails(arts, state);
        detailsRepository.save(details);
        return new ArtSaveResponse(arts.getId(), arts.getArtName());
    }

    @Transactional
    public void update(final Long artsId, final ArtRequest artRequest) {
        Details details = findDetails(artsId);

        details.getArts().update(artRequest.toArts());
        boolean state = getState(artRequest.getEndDate());
        details.update(artRequest.toDetails(details.getArts(), state));
    }

    @Transactional
    public void delete(final Long artsId) {
        Details details = findDetails(artsId);
        detailsRepository.delete(details);
    }

    @Transactional
    public ArtPageResponse find(final Pageable pageable) {

        Page<Details> page = detailsRepository.findDetailsWithArts(pageable);
        return createImageSharePresignedURLByImageURL(page);
    }

    @Transactional
    public ArtResponse findArt(final Long artsId) {
        Details details = findDetails(artsId);
        details.changeState();
        return ArtResponse.of(details, details.getArts());
    }

    @Transactional(readOnly = true)
    public ArtMapResponse findArtOnMap(final Long artsId) {
        Details details = findDetails(artsId);
        return ArtMapResponse.of(details, details.getArts());
    }

    private Details findDetails(final Long artsId) {
        return detailsRepository.findByArtsId(artsId)
                .orElseThrow(DetailsNotFoundException::new);
    }

    private boolean getState(final LocalDate endDate) {
        if(endDate == null || endDate.isAfter(LocalDate.now())) {
            return true;
        }
        return false;
    }

    private ArtPageResponse createImageSharePresignedURLByImageURL(Page<Details> page) {

        List<ArtForAdminResponse> artForAdminResponses = new ArrayList<>();
        for (Details details : page.getContent()) {
            String presignedURL = presignedURLUtils.createImageShareURL(details.getArts().getImageURL(),
                    adminS3Client, adminBucketName);
            artForAdminResponses.add(ArtForAdminResponse.of(details, details.getArts(), presignedURL));
        }
        return new ArtPageResponse(page.getTotalElements(), page.getTotalPages(), artForAdminResponses);
    }
}
