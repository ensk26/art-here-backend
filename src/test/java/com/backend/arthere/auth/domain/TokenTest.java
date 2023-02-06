package com.backend.arthere.auth.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.backend.arthere.fixture.MemberFixtures.회원;
import static com.backend.arthere.fixture.TokenFixtures.토큰;
import static org.assertj.core.api.Assertions.assertThat;

class TokenTest {

    @Test
    @DisplayName("리프레시 토큰 변경에 성공한다.")
    public void 리프레시_토큰_변경_성공() throws Exception {
        //given
        String changeRefreshToken = "changeRefresh";
        Token token = 토큰(회원());

        //when
        token.changeRefreshToken(changeRefreshToken);

        //then
        assertThat(token.getRefreshToken()).isEqualTo(changeRefreshToken);
    }

    @Test
    @DisplayName("리프레시 토큰 값이 같을 때 true를 반환한다.")
    public void 같은_리프레시_토큰_값_판별_성공() throws Exception {
        //given
        Token token = 토큰(회원());
        String sameRefreshToken = token.getRefreshToken();

        //when
        boolean isSame = token.isSameRefreshToken(sameRefreshToken);

        //then
        assertThat(isSame).isTrue();
    }

    @Test
    @DisplayName("리프레시 토큰 값이 다를 때 false를 반환한다.")
    public void 다른_리프레시_토큰_값_판별_성공() throws Exception {
        //given
        String differentRefreshToken = "differentRefresh";
        Token token = 토큰(회원());

        //when
        boolean isSame = token.isSameRefreshToken(differentRefreshToken);

        //then
        assertThat(isSame).isFalse();
    }
}