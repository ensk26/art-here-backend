package com.backend.arthere.member.application;

import com.backend.arthere.member.domain.Member;
import com.backend.arthere.member.domain.MemberRepository;
import com.backend.arthere.member.dto.response.MemberResponse;
import com.backend.arthere.member.exception.MemberNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.backend.arthere.fixture.MemberFixtures.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("아이디로 회원을 조회한다.")
    public void 아이디로_회원_조회() throws Exception {
        //given
        Member member = 회원();

        given(memberRepository.findById((member.getId())))
                .willReturn(Optional.of(member));
        //when
        MemberResponse memberResponse = memberService.findMember(member.getId());

        //then
        assertAll(
                () -> assertThat(memberResponse.getId()).isEqualTo(member.getId()),
                () -> assertThat(memberResponse.getEmail()).isEqualTo(member.getEmail()),
                () -> assertThat(memberResponse.getName()).isEqualTo(member.getName()),
                () -> assertThat(memberResponse.getProfile()).isEqualTo(member.getProfile()),
                () -> assertThat(memberResponse.getSocialType()).isEqualTo(member.getSocialType())
        );
    }

    @Test
    @DisplayName("저장되어 있지 않은 아이디로 회원 조회하려고 하면 예외가 발생한다.")
    public void 저장되어_있지_않은_아이디로_회원_조회시_예외_발생() throws Exception {
        //given
        given(memberRepository.findById(회원_아이디))
                .willThrow(new MemberNotFoundException());
        //when //then
        assertThatThrownBy(() -> memberService.findMember(회원_아이디))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("회원의 이름을 변경한다.")
    public void 회원_이름_변경() throws Exception {
        //given
        Member member = 회원();

        given(memberRepository.findById(member.getId()))
                .willReturn(Optional.of(member));

        //when
        memberService.updateName(member.getId(), 회원_이름_수정);

        //then
        assertAll(
                () -> assertThat(member.getName()).isEqualTo(회원_이름_수정)
        );
    }

}