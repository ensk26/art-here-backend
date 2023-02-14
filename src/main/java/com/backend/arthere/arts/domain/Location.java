package com.backend.arthere.arts.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;
}
