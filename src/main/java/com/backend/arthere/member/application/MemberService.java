package com.backend.arthere.member.application;

import com.backend.arthere.member.domain.Member;
import com.backend.arthere.member.domain.MemberRepository;
import com.backend.arthere.member.dto.response.MemberResponse;
import com.backend.arthere.member.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberResponse findMember(final Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);
        return MemberResponse.from(member);
    }

    @Transactional
    public void updateName(final Long id, final String name) {
        Member member = memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);
        member.changeName(name);
    }
}
