package com.backend.arthere.comment.presentation;

import com.backend.arthere.auth.domain.CurrentUser;
import com.backend.arthere.auth.dto.LoginMember;
import com.backend.arthere.comment.application.CommentService;
import com.backend.arthere.comment.dto.request.CommentRequest;
import com.backend.arthere.comment.dto.response.CommentPageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<Void> save(@PathVariable("postId") final Long postId,
                                     @CurrentUser final LoginMember loginMember,
                                     @Valid @RequestBody CommentRequest commentRequest) {
        Long id = commentService.save(postId, loginMember.getId(), commentRequest);
        return ResponseEntity.created(URI.create("/api/comments/" + id)).build();
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<Void> update(@PathVariable("commentId") final Long commentId,
                                       @CurrentUser final LoginMember loginMember,
                                       @Valid @RequestBody CommentRequest commentRequest) {
        commentService.update(commentId, loginMember.getId(), commentRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> delete(@PathVariable("commentId") final Long commentId,
                                       @CurrentUser final LoginMember loginMember) {
        commentService.delete(commentId, loginMember.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentPageResponse> find(@PathVariable("postId") final Long postId,
                                                    final Pageable pageable,
                                                    @CurrentUser final LoginMember loginMember) {
        CommentPageResponse commentPageResponse = commentService.find(postId, pageable, loginMember.getId());
        return ResponseEntity.ok().body(commentPageResponse);
    }
}
