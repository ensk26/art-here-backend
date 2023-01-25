package com.backend.arthere.auth.jwt;

import com.backend.arthere.auth.application.CustomUserDetailsService;
import com.backend.arthere.auth.domain.TokenRepository;
import com.backend.arthere.auth.exception.FailedTokenAuthenticationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final TokenRepository tokenRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = jwtTokenProvider.resolveToken(request);
        try {
            if(StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
                Long id = jwtTokenProvider.getIdFromToken(token);
                if(tokenRepository.existsByMemberId(id)) {
                    UserDetails userDetails = customUserDetailsService.loadUserById(id);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
                            null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("인증 요청된 사용자 {}", authentication.getName());
                }
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            throw new FailedTokenAuthenticationException();
        }
        filterChain.doFilter(request, response);
    }
}

