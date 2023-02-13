package com.backend.arthere.arts.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class Address {

    @NotNull
    @Column(name = "load_address")
    private String loadAddress;

    @NotNull
    @Column(name = "old_address")
    private String oldAddress;
}
