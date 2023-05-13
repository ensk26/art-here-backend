package com.backend.arthere.global.config;

import com.backend.arthere.auth.application.CustomOAuth2UserService;
import com.backend.arthere.auth.application.CustomUserDetailsService;
import com.backend.arthere.auth.domain.HttpCookieOAuth2AuthorizationRequestRepository;
import com.backend.arthere.auth.domain.TokenRepository;
import com.backend.arthere.auth.handler.OAuth2LoginFailureHandler;
import com.backend.arthere.auth.handler.OAuth2LoginSuccessHandler;
import com.backend.arthere.auth.jwt.JwtAccessDeniedHandler;
import com.backend.arthere.auth.jwt.JwtAuthenticationEntryPoint;
import com.backend.arthere.auth.jwt.JwtTokenAuthenticationFilter;
import com.backend.arthere.auth.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final CustomUserDetailsService customUserDetailsService;
    private final TokenRepository tokenRepository;

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.httpBasic().disable()
                .csrf().disable()
                .headers().frameOptions().disable()
                .and().formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers("/actuator/health").permitAll()
                .antMatchers("/profile").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/api/auth/**", "/oauth2/**", "/login/**", "/api/posts").permitAll()
                .antMatchers(HttpMethod.GET, "/api/posts/**/comments").permitAll()
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .antMatchers("/api/satisfaction/**").hasRole("USER")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorization")
                .authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository)
                .and()
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .and()
                .successHandler(oAuth2LoginSuccessHandler)
                .failureHandler(oAuth2LoginFailureHandler);

        httpSecurity.addFilterBefore(new JwtTokenAuthenticationFilter
                        (jwtTokenProvider, customUserDetailsService, tokenRepository),
                UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers(AUTH_WHITELLIST);
    }

    private static final String[] AUTH_WHITELLIST = {
            "/api/auth/token/issue",
            "/api/image/share",
            "/api/image/media",
            "/api/image/media/address",
            "/api/image/media/name",
            "/api/image/map",
            "/api/art",
            "/api/art/map",
            "/api/satisfaction/list"
    };
}
