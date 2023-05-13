package com.backend.arthere.satisfactions.application;

import com.backend.arthere.arts.domain.Arts;
import com.backend.arthere.arts.domain.ArtsRepository;
import com.backend.arthere.member.domain.Member;
import com.backend.arthere.member.domain.MemberRepository;
import com.backend.arthere.satisfactions.domain.SatisfactionType;
import com.backend.arthere.satisfactions.domain.Satisfactions;
import com.backend.arthere.satisfactions.domain.SatisfactionsRepository;
import com.backend.arthere.satisfactions.dto.request.SaveSatisfactionsRequest;
import com.backend.arthere.satisfactions.dto.response.SatisfactionsCountResponse;
import com.backend.arthere.satisfactions.dto.response.SatisfactionsListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SatisfactionsService {

    private final SatisfactionsRepository satisfactionsRepository;

    private final ArtsRepository artsRepository;

    private final MemberRepository memberRepository;

    //만족도 공유
    public SatisfactionsListResponse findSatisfactionsList(Long id) {

        List<SatisfactionsCountResponse> satisfactionsCount = satisfactionsRepository.findSatisfactionsCount(id);
        SatisfactionsListResponse satisfactionsListResponse = satisfactionsRepository.getTotalToDetailsById(id);
        satisfactionsListResponse.setSatisfactionsCount(satisfactionsCount);
        return satisfactionsListResponse;
    }

    //만족도 추가, 수정
    public void saveSatisfactions(SaveSatisfactionsRequest request, Long userId) {

        Arts arts = artsRepository.getReferenceById(request.getArtsId());   //해당 id가 없을때 발생하는 오류
        Member member = memberRepository.getReferenceById(userId);

        List<Satisfactions> satisfactions = new ArrayList<>();

        for (SatisfactionType type : request.getSatisfactions()) {
            satisfactions.add(new Satisfactions(arts, member, type));
        }
        satisfactionsRepository.saveAll(satisfactions);
    }

    //만족도 삭제
}
