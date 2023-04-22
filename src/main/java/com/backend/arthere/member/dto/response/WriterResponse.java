package com.backend.arthere.member.dto.response;

import com.backend.arthere.member.domain.Member;
import lombok.Getter;

@Getter
public class WriterResponse {

    private Long id;

    private String name;

    private String profile;

    private WriterResponse(final Long id, final String name, final String profile) {
        this.id = id;
        this.name = name;
        this.profile = profile;
    }

    public static WriterResponse from(final Member member) {
        return new WriterResponse(member.getId(), member.getName(), member.getProfile());
    }

}
