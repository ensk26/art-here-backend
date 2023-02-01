package com.backend.arthere.auth.application;

import com.backend.arthere.auth.domain.Token;
import com.backend.arthere.auth.domain.TokenRepository;
import com.backend.arthere.auth.dto.request.TokenRequest;
import com.backend.arthere.auth.dto.response.TokenResponse;
import com.backend.arthere.auth.exception.InvalidRefreshTokenException;
import com.backend.arthere.auth.exception.RefreshTokenNotFoundException;
import com.backend.arthere.auth.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final TokenRepository tokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional(readOnly = true)
    public TokenResponse reissue(final TokenRequest tokenRequest) {
        if(!jwtTokenProvider.validateToken(tokenRequest.getRefreshToken())) {
            throw new InvalidRefreshTokenException();
        }

        Token findRefreshToken = tokenRepository.findByMemberId(tokenRequest.getId())
                .orElseThrow(InvalidRefreshTokenException::new);

        if(!findRefreshToken.isSameRefreshToken(tokenRequest.getRefreshToken())) {
            throw new InvalidRefreshTokenException();
        }

        String accessToken = jwtTokenProvider
                .createAccessToken(String.valueOf(tokenRequest.getId()));

        return new TokenResponse(accessToken);
    }

    @Transactional
    public void logout(final String refreshToken) {
        if(!tokenRepository.existsByRefreshToken(refreshToken)) {
            throw new RefreshTokenNotFoundException();
        }
        tokenRepository.deleteByRefreshToken(refreshToken);
    }
}

