package com.backend.arthere.member.presentation;

import com.backend.arthere.auth.domain.CurrentUser;
import com.backend.arthere.auth.dto.LoginMember;
import com.backend.arthere.member.application.MemberService;
import com.backend.arthere.member.dto.request.MemberNameRequest;
import com.backend.arthere.member.dto.response.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("")
    public ResponseEntity<MemberResponse> findMember(@CurrentUser LoginMember loginMember) {
        MemberResponse memberResponse = memberService.findMember(loginMember.getId());
        return ResponseEntity.ok(memberResponse);
    }

    @PostMapping("/name")
    public ResponseEntity<?> updateName(@CurrentUser LoginMember loginMember,
                                        @Valid @RequestBody MemberNameRequest memberNameRequest) {

        memberService.updateName(loginMember.getId(), memberNameRequest.getName());
        return ResponseEntity.ok().build();
    }
}
