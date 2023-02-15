package com.backend.arthere.arts.presentation;

import com.backend.arthere.arts.application.ArtsService;
import com.backend.arthere.arts.dto.ArtImageByLocationResponse;
import com.backend.arthere.arts.dto.ArtImageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class ArtsController {

    private final ArtsService artsService;

    @GetMapping("/media")
    public ResponseEntity<?> findArtImageByRevisionDate(@RequestParam("offset") @NotNull @Min(0) Long offset,
                                                        @RequestParam("limit") @NotNull @Min(1) Long limit) {

        List<ArtImageResponse> artImageResponses = artsService.findArtImageByRevisionDate(offset, limit);
        return ResponseEntity.ok(artImageResponses);
    }

    @GetMapping("/map")
    public ResponseEntity<?> findArtImageByLocation(@RequestParam("latitude") @NotNull Double latitude,
                                                    @RequestParam("longitude") @NotNull Double longitude) {

        List<ArtImageByLocationResponse> artImageResponses = artsService.findArtImageByLocation(latitude, longitude);
        return ResponseEntity.ok(artImageResponses);
    }
}
