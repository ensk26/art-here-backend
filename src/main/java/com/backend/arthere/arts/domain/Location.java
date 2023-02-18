package com.backend.arthere.arts.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;
}
