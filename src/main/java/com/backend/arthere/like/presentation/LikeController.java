package com.backend.arthere.like.presentation;

import com.backend.arthere.auth.domain.CurrentUser;
import com.backend.arthere.auth.dto.LoginMember;
import com.backend.arthere.like.application.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/like/{postId}")
public class LikeController {

    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<Void> addLike(@PathVariable("postId") final Long postId,
                                       @CurrentUser final LoginMember loginMember) {
        likeService.addLike(postId, loginMember.getId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Long> subtractLike(@PathVariable("postId") final Long postId,
                                           @CurrentUser final LoginMember loginMember) {
        likeService.subtractLike(postId, loginMember.getId());
        return ResponseEntity.ok().build();
    }
}
