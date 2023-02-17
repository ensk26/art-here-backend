package com.backend.arthere.details.application;


import com.backend.arthere.details.domain.Details;
import com.backend.arthere.details.domain.DetailsRepository;
import com.backend.arthere.details.dto.request.ArtRequest;
import com.backend.arthere.details.dto.response.ArtMapResponse;
import com.backend.arthere.details.dto.response.ArtResponse;
import com.backend.arthere.arts.domain.Arts;
import com.backend.arthere.arts.domain.ArtsRepository;
import com.backend.arthere.arts.exception.ArtsNotFoundException;
import com.backend.arthere.details.dto.response.ArtSaveResponse;
import com.backend.arthere.details.exception.DetailsNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DetailsService {
    private final DetailsRepository detailsRepository;
    private final ArtsRepository artsRepository;

    @Transactional
    public ArtSaveResponse save(final ArtRequest artRequest) {
        Arts arts = artsRepository.save(artRequest.toArts());

        boolean state = saveState(artRequest.getEndDate());
        Details details = artRequest.toDetails(arts, state);
        detailsRepository.save(details);
        return new ArtSaveResponse(arts.getId());
    }

    private boolean saveState(final LocalDate endDate) {
        if(endDate == null || endDate.isAfter(LocalDate.now())) {
            return true;
        }
        return false;
    }

    @Transactional
    public ArtResponse findArt(final Long artsId) {
        Arts arts = artsRepository.findById(artsId)
                .orElseThrow(ArtsNotFoundException::new);

        Details details = detailsRepository.findByArts(arts)
                .orElseThrow(DetailsNotFoundException::new);

        details.changeState();
        return new ArtResponse(details, arts);
    }

    @Transactional(readOnly = true)
    public ArtMapResponse findArtOnMap(final Long artsId) {
        Arts arts = artsRepository.findById(artsId)
                .orElseThrow(ArtsNotFoundException::new);

        Details details = detailsRepository.findByArts(arts)
                .orElseThrow(DetailsNotFoundException::new);

        return new ArtMapResponse(details, arts);
    }

}
