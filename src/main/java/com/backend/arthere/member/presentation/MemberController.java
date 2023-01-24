package com.backend.arthere.member.presentation;

import com.backend.arthere.member.application.MemberService;
import com.backend.arthere.member.dto.request.MemberRequest;
import com.backend.arthere.member.dto.response.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("")
    public ResponseEntity<MemberResponse> findMember(MemberRequest memberRequest) {
        MemberResponse memberResponse = memberService.findMember(memberRequest.getId());
        return ResponseEntity.ok(memberResponse);
    }
}
