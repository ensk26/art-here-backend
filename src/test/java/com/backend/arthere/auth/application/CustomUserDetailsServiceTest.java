package com.backend.arthere.auth.application;

import com.backend.arthere.member.domain.Member;
import com.backend.arthere.member.domain.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static com.backend.arthere.fixture.MemberFixtures.회원;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @DisplayName("이메일로 UserDetails 생성한다.")
    public void 이메일로_인증된_회원_생성_성공() throws Exception {
        //given
        Member member = 회원();
        String email = member.getEmail();

        given(memberRepository.findByEmail(email))
                .willReturn(Optional.of(member));

        //when
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        //then
        assertAll(
                () -> assertThat(userDetails.getUsername()).isEqualTo(email),
                () -> verify(memberRepository).findByEmail(email)
        );
    }

    @Test
    @DisplayName("저장되어 있지 않은 이메일로 인증된 회원 요청시 예외 처리한다.")
    public void 저장되어_있지_않은_이메일로_인증된_회원_요청시_예외_처리() throws Exception {
        //given
        String notStoreEmail = "012@gmail.com";

        given(memberRepository.findByEmail(notStoreEmail))
                .willThrow(new UsernameNotFoundException("존재하지 않는 회원입니다."));

        //when //then
        assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername(notStoreEmail))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    @DisplayName("아이디로 UserDetails 생성한다.")
    public void 아이디로_인증된_회원_생성_성공() throws Exception {
        //given
        Member member = 회원();

        given(memberRepository.findById(member.getId()))
                .willReturn(Optional.of(member));

        //when
        UserDetails userDetails = customUserDetailsService.loadUserById(member.getId());

        //then
        assertAll(
                () -> assertThat(userDetails.getUsername()).isEqualTo(member.getEmail()),
                () -> verify(memberRepository).findById(member.getId())
        );
    }

    @Test
    @DisplayName("저장되어 있지 않은 아이디로 인증된 회원 요청시 예외 처리한다.")
    public void 저장되어_있지_않은_아이디로_인증된_회원_요청시_예외_처리() throws Exception {
        //given
        Long id = 1L;

        given(memberRepository.findById(id))
                .willThrow(new UsernameNotFoundException("존재하지 않는 회원입니다."));

        //when //then
        assertThatThrownBy(() -> customUserDetailsService.loadUserById(id))
                .isInstanceOf(UsernameNotFoundException.class);
    }
}