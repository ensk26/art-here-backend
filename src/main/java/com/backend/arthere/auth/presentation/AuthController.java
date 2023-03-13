package com.backend.arthere.auth.presentation;

import com.backend.arthere.auth.application.AuthService;
import com.backend.arthere.auth.dto.request.TokenIssueRequest;
import com.backend.arthere.auth.dto.request.TokenRequest;
import com.backend.arthere.auth.dto.response.TokenIssueResponse;
import com.backend.arthere.auth.dto.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthController {
    private final AuthService authService;

    @PostMapping("/token/reissue")
    public ResponseEntity<TokenResponse> reissue(@RequestBody @Valid final TokenRequest tokenRequest) {
        TokenResponse tokenResponse = authService.reissue(tokenRequest);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/token/issue")
    public ResponseEntity<TokenIssueResponse> issue(@RequestBody @Valid final TokenIssueRequest tokenIssueRequest) {
        TokenIssueResponse tokenIssueResponse = authService.issue(tokenIssueRequest);
        return ResponseEntity.ok(tokenIssueResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody @Valid final TokenRequest tokenRequest) {
        authService.logout(tokenRequest.getRefreshToken());
        return ResponseEntity.ok().build();
    }
    
}
