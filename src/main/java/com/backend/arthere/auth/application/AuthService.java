package com.backend.arthere.auth.application;

import com.backend.arthere.auth.domain.Token;
import com.backend.arthere.auth.domain.TokenRepository;
import com.backend.arthere.auth.dto.request.TokenIssueRequest;
import com.backend.arthere.auth.dto.request.TokenRequest;
import com.backend.arthere.auth.dto.response.TokenIssueResponse;
import com.backend.arthere.auth.dto.response.TokenResponse;
import com.backend.arthere.auth.exception.InvalidRefreshTokenException;
import com.backend.arthere.auth.exception.InvalidTokenException;
import com.backend.arthere.auth.exception.RefreshTokenNotFoundException;
import com.backend.arthere.auth.jwt.JwtTokenProvider;
import com.backend.arthere.member.domain.Member;
import com.backend.arthere.member.domain.MemberRepository;
import com.backend.arthere.member.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final TokenRepository tokenRepository;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional(readOnly = true)
    public TokenResponse reissue(final TokenRequest tokenRequest) {

        validateToken(tokenRequest.getRefreshToken());
        Token token = findToken(tokenRequest.getId());

        if(!token.isSameRefreshToken(tokenRequest.getRefreshToken())) {
            throw new InvalidRefreshTokenException();
        }

        String accessToken = jwtTokenProvider
                .createAccessToken(String.valueOf(tokenRequest.getId()));

        return new TokenResponse(accessToken);
    }

    @Transactional
    public TokenIssueResponse issue(final TokenIssueRequest tokenIssueRequest) {
        matchById(tokenIssueRequest.getId(), tokenIssueRequest.getToken());
        validateToken(tokenIssueRequest.getToken());

        String accessToken = jwtTokenProvider
                .createAccessToken(String.valueOf(tokenIssueRequest.getId()));
        String refreshToken = jwtTokenProvider
                .createRefreshToken(String.valueOf(tokenIssueRequest.getId()));

        Token token = getToken(tokenIssueRequest.getId(), refreshToken);
        token.changeRefreshToken(refreshToken);

        return new TokenIssueResponse(accessToken, refreshToken);
    }

    private Token getToken(final Long id, final String refreshToken) {

        Optional<Token> findToken = tokenRepository.findByMemberId(id);
        if (findToken.isPresent()) {
            return findToken.get();
        }
        Member member = findMember(id);
        return saveToken(refreshToken, member);
    }

    private Token saveToken(final String refreshToken, final Member member) {
        return tokenRepository.save(new Token(refreshToken, member));
    }

    private Token findToken(final Long id) {
        return tokenRepository.findByMemberId(id)
                .orElseThrow(RefreshTokenNotFoundException::new);
    }

    private void validateToken(final String token) {
        if(!jwtTokenProvider.validateToken(token)) {
            throw new InvalidTokenException();
        }
    }

    private Member findMember(final Long id) {
        return memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);
    }

    private void matchById(final Long id, final String token) {
        Long findId = jwtTokenProvider.getIdFromToken(token);

        if(!id.equals(findId)) {
            throw new InvalidTokenException();
        }
    }

    @Transactional
    public void logout(final String refreshToken) {
        if(!tokenRepository.existsByRefreshToken(refreshToken)) {
            throw new RefreshTokenNotFoundException();
        }
        tokenRepository.deleteByRefreshToken(refreshToken);
    }
}

