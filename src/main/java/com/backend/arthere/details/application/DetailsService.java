package com.backend.arthere.details.application;


import com.backend.arthere.details.domain.Details;
import com.backend.arthere.details.domain.DetailsRepository;
import com.backend.arthere.details.dto.request.ArtRequest;
import com.backend.arthere.details.dto.response.ArtMapResponse;
import com.backend.arthere.details.dto.response.ArtResponse;
import com.backend.arthere.arts.domain.Arts;
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

    @Transactional
    public ArtSaveResponse save(final ArtRequest artRequest) {
        Arts arts = artRequest.toArts();

        boolean state = saveState(artRequest.getEndDate());
        Details details = artRequest.toDetails(arts, state);
        detailsRepository.save(details);
        return new ArtSaveResponse(arts.getId(), arts.getArtName());
    }

    private boolean saveState(final LocalDate endDate) {
        if(endDate == null || endDate.isAfter(LocalDate.now())) {
            return true;
        }
        return false;
    }

    @Transactional
    public void update(final Long artsId, final ArtRequest artRequest) {
        Details details = detailsRepository.findByArtsId(artsId)
                .orElseThrow(DetailsNotFoundException::new);

        details.getArts().update(artRequest.toArts());
        boolean state = saveState(artRequest.getEndDate());
        details.update(artRequest.toDetails(details.getArts(), state));
    }

    @Transactional
    public void delete(final Long artsId) {
        Details details = detailsRepository.findByArtsId(artsId)
                .orElseThrow(DetailsNotFoundException::new);
        detailsRepository.delete(details);
    }

    @Transactional
    public ArtResponse findArt(final Long artsId) {
        Details details = detailsRepository.findByArtsId(artsId)
                .orElseThrow(DetailsNotFoundException::new);

        details.changeState();
        return new ArtResponse(details, details.getArts());
    }

    @Transactional(readOnly = true)
    public ArtMapResponse findArtOnMap(final Long artsId) {
        Details details = detailsRepository.findByArtsId(artsId)
                .orElseThrow(DetailsNotFoundException::new);

        return new ArtMapResponse(details, details.getArts());
    }

}
