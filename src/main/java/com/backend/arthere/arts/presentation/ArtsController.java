package com.backend.arthere.arts.presentation;

import com.backend.arthere.arts.application.ArtsService;
import com.backend.arthere.arts.dto.ArtImageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class ArtsController {

    private final ArtsService artsService;

    @GetMapping("/media")
    public ResponseEntity<?> findArtImageByRevisionDate(@RequestParam("offset") Long offset,
                                                        @RequestParam("offset") Long limit) {

        ArtImageResponse artImageResponse = artsService.findArtImageByRevisionDate(offset, limit);
        return ResponseEntity.ok(artImageResponse);
    }
}
