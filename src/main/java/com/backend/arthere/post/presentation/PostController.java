package com.backend.arthere.post.presentation;

import com.backend.arthere.post.application.PostService;
import com.backend.arthere.post.dto.response.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<PostResponse> find(@RequestParam("id") final Long id) {

        return ResponseEntity.ok(postService.find(id));
    }
}
