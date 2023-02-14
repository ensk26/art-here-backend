package com.backend.arthere.auth.domain;

import com.backend.arthere.global.TestConfig;
import com.backend.arthere.member.domain.Member;
import com.backend.arthere.member.domain.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static com.backend.arthere.fixture.MemberFixtures.회원;
import static com.backend.arthere.fixture.TokenFixtures.토큰;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestConfig.class)
class TokenRepositoryTest {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원 아이디로 토큰 조회에 성공한다.")
    public void 회원_아이디로_토큰_조회_성공() throws Exception {
        //given
        Member member = 회원();
        memberRepository.save(member);
        Token token = 토큰(member);
        tokenRepository.save(token);

        //when
        Optional<Token> findToken = tokenRepository.findByMemberId(member.getId());

        //then
        assertThat(findToken).isNotEmpty();
        assertThat(findToken.get().getRefreshToken()).isEqualTo(token.getRefreshToken());
    }

    @Test
    @DisplayName("저장되어 있지 않은 회원 아이디로 토큰 조회시 empty 객체를 반환한다.")
    public void 저장되어_있지_않은_회원_아이디로_토큰_조회() throws Exception {
        //given //when
        Optional<Token> findToken = tokenRepository.findByMemberId(0L);

        //then
        assertThat(findToken).isEmpty();
    }

    @Test
    @DisplayName("회원 아이디의 토큰 정보가 존재하는 경우 true 를 반환한다.")
    public void 회원_아이디의_토큰이_존재할_경우_TRUE_반환() throws Exception {
        //given
        Member member = 회원();
        memberRepository.save(member);
        Token token = 토큰(member);
        tokenRepository.save(token);

        //when
        boolean exists = tokenRepository.existsByMemberId(member.getId());

        //then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("회원 아이디의 토큰 정보가 존재하지 않는 경우 false 를 반환한다.")
    public void 회원_아이디의_토큰이_존재하지_않는_경우_FALSE_반환() throws Exception {
        //given //when
        boolean exists = tokenRepository.existsByMemberId(0L);

        //then
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("리프레시 토큰의 토큰 정보가 존재하는 경우 true 를 반환한다.")
    public void 리프레시_토큰의_토큰_정보가_존재할_경우_TRUE_반환() throws Exception {
        //given
        Member member = 회원();
        memberRepository.save(member);
        Token token = 토큰(member);
        tokenRepository.save(token);

        //when
        boolean exists = tokenRepository.existsByRefreshToken(token.getRefreshToken());

        //then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("리프레시 토큰의 토큰 정보가 존재하지 않는 경우 false 를 반환한다.")
    public void 리프레시_토큰의_토큰_정보가_존재하지_않는_경우_FALSE_반환() throws Exception {
        //given //when
        Token token = 토큰(회원());
        boolean exists = tokenRepository.existsByRefreshToken(token.getRefreshToken());

        //then
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("리프레시 토큰으로 토큰 정보를 삭제한다.")
    public void 리프레시_토큰으로_토큰_삭제_성공() throws Exception {
        //given
        Member member = 회원();
        memberRepository.save(member);
        Token token = 토큰(member);
        tokenRepository.save(token);
        //when
        tokenRepository.deleteByRefreshToken(token.getRefreshToken());
        //then
        assertThat(tokenRepository.existsByRefreshToken(token.getRefreshToken())).isFalse();
    }

}