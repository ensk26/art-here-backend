package com.backend.arthere.satisfactions.domain;

import com.backend.arthere.arts.domain.*;
import com.backend.arthere.arts.dto.ArtImageByRevisionDateRequest;
import com.backend.arthere.arts.dto.ArtImageResponse;
import com.backend.arthere.global.config.JpaConfig;
import com.backend.arthere.global.config.QueryDslConfig;
import com.backend.arthere.member.domain.Member;
import com.backend.arthere.member.domain.MemberRepository;
import com.backend.arthere.member.domain.Role;
import com.backend.arthere.member.domain.SocialType;
import com.backend.arthere.satisfactions.dto.response.SatisfactionsResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@Import({QueryDslConfig.class, JpaConfig.class})
class SatisfactionsRepositoryTest {

    @Autowired
    SatisfactionsRepository satisfactionsRepository;

    @Autowired
    ArtsRepository artsRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    void 만족도_총_개수() {


        //given
        Long arts_id = 1L;
        SatisfactionType satisfactionType = SatisfactionType.valueOfName("멋져요");


        List<Satisfactions> satisfactions = new ArrayList<>();

        artsSaveData();
        memberSaveData();
        Arts arts = artsRepository.getReferenceById(arts_id);



        for (int i = 1; i <= 5; i++) {
            Member member = memberRepository.getReferenceById(Long.parseLong(String.valueOf(i)));
            satisfactions.add(new Satisfactions(arts, member, satisfactionType));
        }

        satisfactionsRepository.saveAll(satisfactions);

        //when
        List<SatisfactionsResponse> result = satisfactionsRepository.findSatisfactions(arts_id);
        System.out.println("result = " + result.get(0).getSatisfaction()+" "+result.get(0).getCount());

        ///then

    }

    private void artsSaveData() {

        String artName = "모래작품";
        String imageURL = "image/sand";
        Address address = new Address("loadAddress");

        artsRepository.save(
                Arts.builder()
                        .artName(artName)
                        .imageURL(imageURL)
                        .location(new Location(37.564878339197044, 126.9758637182802))
                        .address(address)
                        .category(Category.PICTURE).build());

    }

    private void memberSaveData() {
        for (int i = 1; i <= 5; i++) {
            memberRepository.save(
                    new Member(Long.parseLong(String.valueOf(i)),"email","name","profile", Role.USER, SocialType.GOOGLE));
        }
    }

}