package com.backend.arthere.satisfactions.presentation;

import com.backend.arthere.auth.domain.CurrentUser;
import com.backend.arthere.auth.dto.LoginMember;
import com.backend.arthere.satisfactions.application.SatisfactionsService;
import com.backend.arthere.satisfactions.dto.request.AddSatisfactionsRequest;
import com.backend.arthere.satisfactions.dto.request.CreateSatisfactionsRequest;
import com.backend.arthere.satisfactions.dto.response.SatisfactionsListResponse;
import com.backend.arthere.satisfactions.dto.response.SatisfactionsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.net.URI;

@RestController
@Validated
@RequestMapping("/api/satisfaction")
@RequiredArgsConstructor
public class SatisfactionsController {

    private final SatisfactionsService satisfactionsService;

    @GetMapping("/list")
    public ResponseEntity<?> findSatisfactionsList(@Min(value = 1, message = "id는 1이상의 수 입니다.") Long id) {

        SatisfactionsListResponse responses = satisfactionsService.findSatisfactionsList(id);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("")
    public ResponseEntity<?> findSatisfactions(@Min(value = 1, message = "id는 1이상의 수 입니다.")
                                               @RequestParam("id") Long artId,
                                               @CurrentUser LoginMember loginMember) {

        SatisfactionsResponse responses = satisfactionsService.findSatisfactions(artId, loginMember.getId());
        return ResponseEntity.ok(responses);
    }


    @PostMapping("")
    public ResponseEntity<?> createSatisfactions(@Valid @RequestBody CreateSatisfactionsRequest request,
                                                 @CurrentUser LoginMember loginMember) throws Exception {

        satisfactionsService.createSatisfactions(request, loginMember.getId());
        return ResponseEntity.created(URI.create("/api/satisfaction?" + request.getArtsId())).build();
    }

    @PatchMapping("")
    public ResponseEntity<?> addSatisfactions(@Valid @RequestBody AddSatisfactionsRequest request,
                                              @CurrentUser LoginMember loginMember) throws Exception {

        satisfactionsService.addSatisfactions(request, loginMember.getId());
        return ResponseEntity.ok().build();
    }


    //게시물 목록 구현
}
