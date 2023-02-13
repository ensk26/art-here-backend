package com.backend.arthere.arts.domain;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class Location {

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;
}
