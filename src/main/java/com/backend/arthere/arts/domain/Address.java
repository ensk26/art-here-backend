package com.backend.arthere.arts.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @NotNull
    @Column(name = "road_address")
    private String roadAddress;

}
