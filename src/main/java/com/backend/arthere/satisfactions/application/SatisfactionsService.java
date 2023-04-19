package com.backend.arthere.satisfactions.application;

import com.backend.arthere.satisfactions.domain.SatisfactionsRepository;
import com.backend.arthere.satisfactions.dto.response.SatisfactionsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SatisfactionsService {

    private final SatisfactionsRepository satisfactionsRepository;

    //만족도 공유
    public List<SatisfactionsResponse> findSatisfactions(Long id) {

        return satisfactionsRepository.findSatisfactions(id);
    }

    //만족도 추가, 수정

    //만족도 삭제
}
