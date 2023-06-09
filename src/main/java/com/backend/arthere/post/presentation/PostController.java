package com.backend.arthere.post.presentation;

import com.backend.arthere.auth.domain.CurrentUser;
import com.backend.arthere.auth.dto.LoginMember;
import com.backend.arthere.post.application.PostService;
import com.backend.arthere.post.dto.response.PostResponse;
import com.backend.arthere.post.dto.response.PostsResponse;
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
    public ResponseEntity<PostResponse> find(@RequestParam("id") final Long id,
                                             @CurrentUser final LoginMember loginMember) {

        return ResponseEntity.ok(postService.find(id, loginMember.getId()));
    }

    @GetMapping("/list")
    public ResponseEntity<PostsResponse> findPosts(@RequestParam("id") Long artId, String sorting, String cursor) {

        return ResponseEntity.ok(postService.findPosts(artId, sorting, cursor));
    }

    // post 삭제

    // post 수정
}
