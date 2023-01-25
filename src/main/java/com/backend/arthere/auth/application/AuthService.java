package com.backend.arthere.auth.application;

import com.backend.arthere.auth.domain.Token;
import com.backend.arthere.auth.domain.TokenRepository;
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
    public TokenResponse reissue(String refreshToken) {
        if(!jwtTokenProvider.validateToken(refreshToken)) {
            throw new InvalidRefreshTokenException();
        }

        Long id = jwtTokenProvider.getIdFromToken(refreshToken);
        Token findRefreshToken = tokenRepository.findByMemberId(id)
                .orElseThrow(InvalidRefreshTokenException::new);

        if(!findRefreshToken.isSameRefreshToken(refreshToken)) {
            throw new InvalidRefreshTokenException();
        }

        String accessToken = jwtTokenProvider.createAccessToken(String.valueOf(findRefreshToken.getId()));
        return new TokenResponse(accessToken);
    }

    @Transactional
    public void logout(String refreshToken) {
        if(!tokenRepository.existsByRefreshToken(refreshToken)) {
            throw new RefreshTokenNotFoundException();
        }
        tokenRepository.deleteByRefreshToken(refreshToken);
    }
}

