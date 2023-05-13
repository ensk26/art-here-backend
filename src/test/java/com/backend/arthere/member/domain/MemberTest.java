package com.backend.arthere.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.backend.arthere.fixture.MemberFixtures.회원;
import static com.backend.arthere.fixture.MemberFixtures.회원_아이디;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class MemberTest {

    @Test
    @DisplayName("권한 이름을 반환한다.")
    public void 권한_이름_반환() throws Exception {
        //given
        Member member = 회원();
        String roleName = Role.USER.getRoleName();
        //when
        String findRoleName = member.getRoleName();
        //then
        assertThat(findRoleName).isEqualTo(roleName);
    }

    @Test
    @DisplayName("이름을 변경한다.")
    public void 이름_변경() throws Exception {
        //given
        Member member = 회원();
        String name = "이름변경";
        //when
        member.changeName(name);
        //then
        assertThat(name).isEqualTo(member.getName());
    }

    @Test
    @DisplayName("아이디가 같을 때 true 를 반환한다.")
    public void 아이디_비교() throws Exception {
        //given
        Member member = 회원(회원_아이디);
        //when
        boolean sameId = member.isSameId(회원_아이디);
        //then
        assertThat(sameId).isTrue();
    }
}