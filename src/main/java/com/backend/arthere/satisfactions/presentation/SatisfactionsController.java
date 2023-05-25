package com.backend.arthere.satisfactions.presentation;

import com.backend.arthere.auth.domain.CurrentUser;
import com.backend.arthere.auth.dto.LoginMember;
import com.backend.arthere.satisfactions.application.SatisfactionsService;
import com.backend.arthere.satisfactions.dto.request.SaveSatisfactionsRequest;
import com.backend.arthere.satisfactions.dto.response.SatisfactionsListResponse;
import com.backend.arthere.satisfactions.dto.response.SatisfactionsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/api/satisfaction")
@RequiredArgsConstructor
public class SatisfactionsController {

    private final SatisfactionsService satisfactionsService;

    @GetMapping("/list")
    public ResponseEntity<?> findSatisfactionsList(@Valid @Min(value = 1, message = "id는 1이상의 수 입니다.") Long id) {

        SatisfactionsListResponse responses = satisfactionsService.findSatisfactionsList(id);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("")
    public ResponseEntity<?> findSatisfactions(@Valid @Min(value = 1, message = "id는 1이상의 수 입니다.") Long artId,
                                               @CurrentUser LoginMember loginMember) {

        SatisfactionsResponse responses = satisfactionsService.findSatisfactions(artId, loginMember.getId());
        return ResponseEntity.ok(responses);
    }

    //만족도 추가, 수정
    @PostMapping("")
    public ResponseEntity<?> saveSatisfactions(@Valid @RequestBody SaveSatisfactionsRequest request,
                                               @CurrentUser LoginMember loginMember) {

        satisfactionsService.saveSatisfactions(request, loginMember.getId());
        //return ResponseEntity.created().build();
        return null;
    }


    //만족도 삭제
}
