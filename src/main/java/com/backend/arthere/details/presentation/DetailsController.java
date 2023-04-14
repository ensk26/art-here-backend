package com.backend.arthere.details.presentation;


import com.backend.arthere.details.application.DetailsService;
import com.backend.arthere.details.dto.request.ArtRequest;
import com.backend.arthere.details.dto.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class DetailsController {

    private final DetailsService detailsService;

    @PostMapping("/admin/art")
    public ResponseEntity<ArtSaveResponse> save(@Valid @RequestBody ArtRequest artRequest) {
        ArtSaveResponse artSaveResponse = detailsService.save(artRequest);
        return ResponseEntity.ok(artSaveResponse);
    }

    @PatchMapping("/admin/art")
    public ResponseEntity<Void> update(@RequestParam("id") Long artsId,
                                              @Valid @RequestBody ArtRequest artRequest) {
        detailsService.update(artsId, artRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/admin/art")
    public ResponseEntity<Void> delete(@RequestParam("id") Long artsId) {
        detailsService.delete(artsId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/admin/art")
    public ResponseEntity<ArtPageResponse> find(@RequestParam(value = "name", required = false) final String artName,
                                                final Pageable pageable) {
        ArtPageResponse artPageResponse = detailsService.find(artName, pageable);
        return ResponseEntity.ok(artPageResponse);
    }

    @GetMapping("/art")
    public ResponseEntity<ArtResponse> findArt(@RequestParam("id") Long artsId) {
        ArtResponse artResponse = detailsService.findArt(artsId);
        return ResponseEntity.ok(artResponse);
    }

    @GetMapping("/art/map")
    public ResponseEntity<ArtMapResponse> findArtOnMap(@RequestParam("id") Long artsId) {
        ArtMapResponse artMapResponse = detailsService.findArtOnMap(artsId);
        return ResponseEntity.ok(artMapResponse);
    }
}
