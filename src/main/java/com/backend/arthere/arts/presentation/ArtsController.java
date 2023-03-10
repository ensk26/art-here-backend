package com.backend.arthere.arts.presentation;

import com.backend.arthere.arts.application.ArtsService;
import com.backend.arthere.arts.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
            @Valid final ArtImageByAddressRequest request)  {

        return ResponseEntity.ok(artsService.searchArtImageByAddress(request));
    }

    @GetMapping("/media/name")
    public ResponseEntity<?> searchArtImageByArtName(@Valid ArtImageByArtNameRequest request) {

        return ResponseEntity.ok(artsService.searchArtImageByArtName(request));
    }

    @GetMapping("/map")
    public ResponseEntity<?> findArtImageByLocation(@RequestParam("latitude") @NotNull Double latitude,
                                                    @RequestParam("longitude") @NotNull Double longitude) {

        List<ArtImageByLocationResponse> artImageResponses = artsService.findArtImageByLocation(latitude, longitude);
        return ResponseEntity.ok(artImageResponses);
    }

}
