package com.backend.arthere.member.domain;

import com.backend.arthere.global.config.JpaConfig;
import com.backend.arthere.global.config.QueryDslConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static com.backend.arthere.fixture.MemberFixtures.회원;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
@Import({QueryDslConfig.class, JpaConfig.class})
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("이메일로 회원을 조회한다.")
    public void 이메일로_회원_조회() throws Exception {
        //given
        Member member = memberRepository.save(회원());

        //when
        Optional<Member> findMember = memberRepository.findByEmail(member.getEmail());

        //then
        assertThat(findMember).isNotEmpty();
        assertThat(findMember.get().getId()).isEqualTo(member.getId());
        assertThat(findMember.get().getName()).isEqualTo(member.getName());

    }

    @Test
    @DisplayName("저장되어 있지 않은 이메일로 회원 조회시 empty 객체를 반환한다.")
    public void 저장되어_있지_않은_이메일로_회원_조회() throws Exception {
        //given
        Member member = 회원();

        //when
        Optional<Member> findMember = memberRepository.findByEmail(member.getEmail());

        //then
        assertThat(findMember).isEmpty();

    }

    @Test
    @DisplayName("소셜 타입과 이메일로 회원을 조회한다.")
    public void 소셜_타입과_이메일로_회원_조회() throws Exception {
        //given
        Member member = memberRepository.save(회원());

        //when
        Optional<Member> findMember = memberRepository
                .findBySocialTypeAndEmail(member.getSocialType(), member.getEmail());
        //then
        assertThat(findMember).isNotEmpty();
        assertThat(findMember.get().getId()).isEqualTo(member.getId());
        assertThat(findMember.get().getEmail()).isEqualTo(member.getEmail());
    }


}