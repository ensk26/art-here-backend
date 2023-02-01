package com.backend.arthere.auth.jwt;

import com.backend.arthere.auth.application.CustomUserDetailsService;
import com.backend.arthere.auth.domain.TokenRepository;
import com.backend.arthere.auth.domain.UserPrincipal;
import com.backend.arthere.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;

import static com.backend.arthere.fixture.MemberFixtures.회원;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JwtTokenAuthenticationFilterTest {
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private CustomUserDetailsService customUserDetailsService;
    @InjectMocks
    private JwtTokenAuthenticationFilter authenticationFilter;

    private static final Long ID = 1L;
    private static final String ACCESS_TOKEN = "testAccess";

    @Test
    @DisplayName("회원 인증에 성공한다.")
    public void 회원_인증_성공() throws Exception {
        //given
        Member member = 회원();
        UserDetails userDetails = UserPrincipal.create(member);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + ACCESS_TOKEN);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain mockFilterChain = new MockFilterChain();

        given(jwtTokenProvider.resolveToken(request))
                .willReturn(ACCESS_TOKEN);
        given(jwtTokenProvider.validateToken(ACCESS_TOKEN))
                .willReturn(true);
        given(jwtTokenProvider.getIdFromToken(ACCESS_TOKEN))
                .willReturn(ID);
        given(tokenRepository.existsByMemberId(ID))
                .willReturn(true);
        given(customUserDetailsService.loadUserById(ID))
                .willReturn(userDetails);

        //when
        authenticationFilter.doFilterInternal(request, response, mockFilterChain);

        //then
        assertAll(
                () -> assertThat(mockFilterChain.getRequest()).isEqualTo(request),
                () -> assertThat(mockFilterChain.getResponse()).isEqualTo(response),
                () -> verify(jwtTokenProvider).resolveToken(request),
                () -> verify(jwtTokenProvider).validateToken(ACCESS_TOKEN),
                () -> verify(jwtTokenProvider).getIdFromToken(ACCESS_TOKEN),
                () -> verify(tokenRepository).existsByMemberId(ID),
                () -> verify(customUserDetailsService).loadUserById(ID)
        );
    }

    @Test
    @DisplayName("올바르지 않은 헤더로 회원 인증을 요청했을 때 다음 과정을 진행하지 않는다.")
    public void 올바르지_않은_헤더로_회원_인증_요청시_처리() throws Exception {
        //given
        Member member = 회원();
        UserDetails userDetails = UserPrincipal.create(member);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("test", "Bearer " + ACCESS_TOKEN);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain mockFilterChain = new MockFilterChain();

        given(jwtTokenProvider.resolveToken(request))
                .willReturn(null);

        //when
        authenticationFilter.doFilterInternal(request, response, mockFilterChain);

        //then
        assertAll(
                () -> assertThat(mockFilterChain.getRequest()).isEqualTo(request),
                () -> assertThat(mockFilterChain.getResponse()).isEqualTo(response),
                () -> verify(jwtTokenProvider).resolveToken(request),
                () -> verify(jwtTokenProvider, times(0)).validateToken(ACCESS_TOKEN),
                () -> verify(jwtTokenProvider, times(0)).getIdFromToken(ACCESS_TOKEN),
                () -> verify(tokenRepository, times(0)).existsByMemberId(ID),
                () -> verify(customUserDetailsService, times(0)).loadUserById(ID)
        );
    }
}