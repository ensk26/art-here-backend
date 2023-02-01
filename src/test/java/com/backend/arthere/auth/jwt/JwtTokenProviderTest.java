package com.backend.arthere.auth.jwt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenProviderTest {

    private static final String KEY = "123456789101234567891012345678910";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 30000;
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 30000;
    private static final String ID = "123";
    private final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(KEY,
            ACCESS_TOKEN_EXPIRE_TIME, REFRESH_TOKEN_EXPIRE_TIME);

    @Test
    @DisplayName("올바른 액세스 토큰을 생성한다.")
    public void 액세스_토큰_생성_성공() throws Exception {
        //given
        //when
        String accessToken = jwtTokenProvider.createAccessToken(ID);
        //then
        assertThat(accessToken).isNotNull();
        assertThat(accessToken.split("\\.")).hasSize(3);
    }

    @Test
    @DisplayName("올바른 리프레시 토큰을 생성한다.")
    public void 리프레시_토큰_생성_성공() throws Exception {
        //given
        //when
        String refreshToken = jwtTokenProvider.createRefreshToken(ID);
        //then
        assertThat(refreshToken).isNotNull();
        assertThat(refreshToken.split("\\.")).hasSize(3);
    }

    @Test
    @DisplayName("액세스 토큰에서 올바른 아이디를 가져온다.")
    public void 토큰에서_올바른_아이디_가져오기_성공() throws Exception {
        //given
        String accessToken = jwtTokenProvider.createAccessToken(ID);
        //when
        Long id = jwtTokenProvider.getIdFromToken(accessToken);
        //then
        assertThat(String.valueOf(id)).isEqualTo(ID);
    }

    @Test
    @DisplayName("헤더에서 액세스 토큰을 가져오는데 성공한다.")
    public void 헤더에서_액세스_토큰_추출_성공() throws Exception {
        //given
        String accessToken = jwtTokenProvider.createAccessToken(ID);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + accessToken);

        //when
        String resolveToken = jwtTokenProvider.resolveToken(request);
        //then
        assertThat(resolveToken).isEqualTo(accessToken);
    }

    @Test
    @DisplayName("헤더가 일치하지 않으면 null을 반환한다.")
    public void 일치하지_않는_헤더_판별_성공() throws Exception {
        //given
        String accessToken = jwtTokenProvider.createAccessToken(ID);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("test", "Bearer " + accessToken);

        //when
        String resolveToken = jwtTokenProvider.resolveToken(request);
        //then
        assertThat(resolveToken).isEqualTo(null);
    }

    @Test
    @DisplayName("유효한 토큰은 true 처리한다.")
    public void 유효한_토큰_판별_성공() throws Exception {
        //given
        String accessToken = jwtTokenProvider.createAccessToken(ID);
        //when
        boolean validateToken = jwtTokenProvider.validateToken(accessToken);
        //then
        assertThat(validateToken).isTrue();
    }

    @Test
    @DisplayName("기간이 만료된 토큰은 false 처리한다.")
    public void 기간이_만료된_토큰_판별_성공() throws Exception {
        //given
        String accessToken = jwtTokenProvider.createToken(ID, 0);

        //when
        boolean validateToken = jwtTokenProvider.validateToken(accessToken);

        //then
        assertThat(validateToken).isFalse();
    }
}