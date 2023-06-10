package com.backend.arthere.post.presentation;

import com.backend.arthere.auth.domain.CurrentUser;
import com.backend.arthere.auth.dto.LoginMember;
import com.backend.arthere.post.application.PostService;
import com.backend.arthere.post.dto.response.PostResponse;
import com.backend.arthere.post.dto.response.PostsResponse;
import com.backend.arthere.post.dto.resquest.CreatePostsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

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

    @PostMapping
    public ResponseEntity<?> createPost(@Valid @RequestBody CreatePostsRequest request,
                                        @CurrentUser LoginMember loginMember) {
        postService.createPost(request, loginMember.getId());
        return ResponseEntity.created(URI.create("")).build();
    }

    // post 삭제

    // post 수정
}
