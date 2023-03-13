package com.backend.arthere.auth.application;

import com.backend.arthere.auth.domain.Token;
import com.backend.arthere.auth.domain.TokenRepository;
import com.backend.arthere.auth.dto.request.TokenIssueRequest;
import com.backend.arthere.auth.dto.request.TokenRequest;
import com.backend.arthere.auth.dto.response.TokenIssueResponse;
import com.backend.arthere.auth.dto.response.TokenResponse;
import com.backend.arthere.auth.exception.InvalidTokenException;
import com.backend.arthere.auth.exception.RefreshTokenNotFoundException;
import com.backend.arthere.auth.jwt.JwtTokenProvider;

import com.backend.arthere.member.domain.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static com.backend.arthere.fixture.MemberFixtures.회원;
import static com.backend.arthere.fixture.TokenFixtures.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @InjectMocks
    private AuthService authService;


    @Test
    @DisplayName("리프레시 토큰으로 액세스 토큰 재발급에 성공한다.")
    public void 리프레시_토큰으로_액세스_토큰_재발급_성공() throws Exception {
        //given
        TokenRequest tokenRequest = 토큰_요청();
        Token refreshToken = 토큰(회원());
        TokenResponse tokenResponse = 토큰_응답();

        given(jwtTokenProvider.validateToken(tokenRequest.getRefreshToken()))
                .willReturn(true);
        given(tokenRepository.findByMemberId(tokenRequest.getId()))
                .willReturn(Optional.of(refreshToken));
        given(jwtTokenProvider.createAccessToken(String.valueOf(tokenRequest.getId())))
                .willReturn(tokenResponse.getAccessToken());

        //when
        TokenResponse reissue = authService.reissue(tokenRequest);

        //then
        assertAll(
                () -> assertThat(reissue.getAccessToken()).isEqualTo(tokenResponse.getAccessToken()),
                () -> verify(jwtTokenProvider).validateToken(tokenRequest.getRefreshToken()),
                () -> verify(jwtTokenProvider).createAccessToken(String.valueOf(tokenRequest.getId())),
                () -> verify(tokenRepository).findByMemberId(tokenRequest.getId())
        );

    }

    @Test
    @DisplayName("리프레시 토큰 없이 액세스 토큰 재발급하려고 하면 예외가 발생한다.")
    public void 리프레시_토큰_없이_액세스_토큰_재발급_예외_발생() throws Exception {
        //given
        TokenRequest noRefreshToken = 리프레시_토큰_없이_요청();

        given(jwtTokenProvider.validateToken(noRefreshToken.getRefreshToken()))
                .willThrow(new InvalidTokenException());

        //when //then
        assertThatThrownBy(() -> authService.reissue(noRefreshToken))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    @DisplayName("만료된 리프레시 토큰으로 액세스 토큰 재발급하려고 하면 예외가 발생한다.")
    public void 만료된_리프레시_토큰으로_액세스_토큰_재발급_예외_발생() throws Exception {
        //given
        TokenRequest expiredRefreshToken = 토큰_요청();
        given(jwtTokenProvider.validateToken(expiredRefreshToken.getRefreshToken()))
                .willThrow(new InvalidTokenException());

        //when //then
        assertThatThrownBy(() -> authService.reissue(expiredRefreshToken))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    @DisplayName("다른 회원의 리프레시 토큰으로 액세스 토큰 재발급하려고 하면 예외가 발생한다.")
    public void 유효하지_않은_리프레시_토큰으로_액세스_토큰_재발급_예외_발생() throws Exception {
        //given
        TokenRequest expiredRefreshToken = 토큰_요청();

        given(jwtTokenProvider.validateToken(expiredRefreshToken.getRefreshToken()))
                .willReturn(true);
        given(tokenRepository.findByMemberId(expiredRefreshToken.getId()))
                .willThrow(new RefreshTokenNotFoundException());

        //when //then
        assertThatThrownBy(() -> authService.reissue(expiredRefreshToken))
                .isInstanceOf(RefreshTokenNotFoundException.class);
    }
    
    @Test
    @DisplayName("토큰 발급에 성공한다.")
    public void 토큰_발급_성공() throws Exception {
        //given
        TokenIssueRequest tokenIssueRequest = 토큰_발급_요청();
        TokenIssueResponse tokenIssueResponse = 토큰_발급_응답();

        given(jwtTokenProvider.validateToken(tokenIssueRequest.getToken()))
                .willReturn(true);
        given(jwtTokenProvider.getIdFromToken(tokenIssueRequest.getToken()))
                .willReturn(tokenIssueRequest.getId());
        given(jwtTokenProvider.createAccessToken(String.valueOf(tokenIssueRequest.getId())))
                .willReturn(tokenIssueResponse.getAccessToken());
        given(jwtTokenProvider.createRefreshToken(String.valueOf(tokenIssueRequest.getId())))
                .willReturn(tokenIssueResponse.getRefreshToken());

        given(tokenRepository.findByMemberId(tokenIssueRequest.getId()))
                .willReturn(Optional.empty());
        given(tokenRepository.save(any()))
                .willReturn(토큰(회원()));
        given(memberRepository.findById(tokenIssueRequest.getId()))
                .willReturn(Optional.of(회원()));

        //when
        TokenIssueResponse issue = authService.issue(tokenIssueRequest);
        //then
        assertAll(
                () -> assertThat(issue.getAccessToken()).isEqualTo(tokenIssueResponse.getAccessToken()),
                () -> assertThat(issue.getAccessToken()).isEqualTo(tokenIssueResponse.getAccessToken())
        );
    }
    
    @Test
    @DisplayName("기간이 만료된 토큰으로 발급 요청시 예외가 발생한다.")
    public void 기간이_만료된_토큰으로_발급_요청시_예외_발생() throws Exception {
        //given
        TokenIssueRequest tokenIssueRequest = 토큰_발급_요청();

        given(jwtTokenProvider.getIdFromToken(tokenIssueRequest.getToken()))
                .willReturn(tokenIssueRequest.getId());
        given(jwtTokenProvider.validateToken(tokenIssueRequest.getToken()))
                .willReturn(false);
        //when //then
        assertThatThrownBy(() -> authService.issue(tokenIssueRequest))
                .isInstanceOf(InvalidTokenException.class);
    }
    
    @Test
    @DisplayName("올바르지 않은 사용자가 토큰 발급 요청시 예외가 발생한다.")
    public void 유효하지_않은_사용자가_발급_요청시_예외_발생() throws Exception {
        //given
        TokenIssueRequest tokenIssueRequest = 토큰_발급_요청();
        given(jwtTokenProvider.getIdFromToken(tokenIssueRequest.getToken()))
                .willThrow(new InvalidTokenException());

        //when //then
        assertThatThrownBy(() -> authService.issue(tokenIssueRequest))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    @DisplayName("리프레시 토큰으로 로그아웃에 성공한다.")
    public void 리프레시_토큰으로_로그아웃_성공() throws Exception {
        //given
        String refreshToken = "testRefresh";

        given(tokenRepository.existsByRefreshToken(refreshToken))
                .willReturn(true);
        willDoNothing().given(tokenRepository).deleteByRefreshToken(refreshToken);

        //when
        authService.logout(refreshToken);

        // then
        assertAll(
                () -> verify(tokenRepository).existsByRefreshToken(refreshToken),
                () -> verify(tokenRepository).deleteByRefreshToken(refreshToken)
        );
    }

    @Test
    @DisplayName("리프레시 토큰 없이 로그아웃하려고 하면 예외가 발생한다.")
    public void 리프레시_토큰_없이_로그아웃시_예외_발생() throws Exception {
        //given
        given(tokenRepository.existsByRefreshToken(null))
                .willThrow(new RefreshTokenNotFoundException());

        //when //then
        assertThatThrownBy(() -> authService.logout(null))
                .isInstanceOf(RefreshTokenNotFoundException.class);
    }

}