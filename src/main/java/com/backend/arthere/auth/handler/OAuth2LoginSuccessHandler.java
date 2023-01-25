package com.backend.arthere.auth.handler;

import com.backend.arthere.auth.domain.HttpCookieOAuth2AuthorizationRequestRepository;
import com.backend.arthere.auth.domain.Token;
import com.backend.arthere.auth.domain.TokenRepository;
import com.backend.arthere.auth.domain.UserPrincipal;
import com.backend.arthere.auth.jwt.JwtTokenProvider;
import com.backend.arthere.auth.util.CookieUtils;
import com.backend.arthere.member.domain.Member;
import com.backend.arthere.member.domain.MemberRepository;
import com.backend.arthere.member.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static com.backend.arthere.auth.domain.HttpCookieOAuth2AuthorizationRequestRepository.REFRESH_TOKEN_COOKIE_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;
    private final MemberRepository memberRepository;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    @Value("${oauth.redirect-uri}")
    private String redirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 Login 성공했습니다");
        String targetUrl = determineTargetUrl(request, response, authentication);
        if(response.isCommitted()) {
            log.info("응답이 이미 커밋되었습니다. {}로 리다이렉션 할 수 없습니다", targetUrl);
            return;
        }
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request,response);
    }

    private String saveRefreshToken(UserPrincipal principal) {
        String refreshToken = jwtTokenProvider
                .createRefreshToken(String.valueOf(principal.getId()));

        Optional<Token> token = tokenRepository.findByMemberId(principal.getId());
        if (token.isPresent()) {
            token.get().changeRefreshToken(refreshToken);
        }
        else {
            Member member = memberRepository.findById(principal.getId())
                    .orElseThrow(MemberNotFoundException::new);
            log.info("내가 찾은 멤버 {}", member.getEmail());
            tokenRepository.save(new Token(refreshToken, member));

        }
        return refreshToken;
    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        String targetUrl = redirectUri;
        String accessToken = jwtTokenProvider
                .createAccessToken(String.valueOf(principal.getId()));
        String refreshToken = saveRefreshToken(principal);
        CookieUtils.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken, 180);

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("accessToken", accessToken)
                .queryParam("id", principal.getId())
                .build().toUriString();
    }
}
