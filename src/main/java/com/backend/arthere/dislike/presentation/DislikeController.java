package com.backend.arthere.dislike.presentation;

import com.backend.arthere.auth.domain.CurrentUser;
import com.backend.arthere.auth.dto.LoginMember;
import com.backend.arthere.dislike.application.DislikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/dislike/{postId}")
public class DislikeController {
    private final DislikeService dislikeService;

    @PostMapping
    public ResponseEntity<Void> addDislike(@PathVariable("postId") final Long postId,
                                           @CurrentUser final LoginMember loginMember) {
        dislikeService.addDislike(postId, loginMember.getId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> subtractDislike(@PathVariable("postId") final Long postId,
                                                @CurrentUser final LoginMember loginMember) {
        dislikeService.subtractDislike(postId, loginMember.getId());
        return ResponseEntity.ok().build();
    }
}
