package com.backend.arthere.arts.presentation;

import com.backend.arthere.arts.application.ArtsService;
import com.backend.arthere.arts.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class ArtsController {

    private final ArtsService artsService;

    @GetMapping("/media")
    public ResponseEntity<?> findArtImageByRevisionDate(@Valid ArtImageByRevisionDateRequest request) {

        ArtImageByRevisionDateResponse responses = artsService.findArtImageByRevisionDate(request);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/media/address")
    public ResponseEntity<?> searchArtImageByAddress(
            @Valid final ArtImageByAddressRequest request) {

        return ResponseEntity.ok(artsService.searchArtImageByAddress(request));
    }

    @GetMapping("/media/name")
    public ResponseEntity<?> searchArtImageByArtName(@Valid ArtImageByArtNameRequest request) {

        return ResponseEntity.ok(artsService.searchArtImageByArtName(request));
    }

    @GetMapping("/map")
    public ResponseEntity<?> findArtImageByLocation(@Valid ArtImageByLocationRequest request) {

        List<ArtImageByLocationResponse> artImageResponses = artsService
                .findArtImageByLocation(request);
        return ResponseEntity.ok(artImageResponses);
    }

}
