package com.backend.arthere.arts.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @NotNull
    @Column(name = "load_address")
    private String loadAddress;

    @NotNull
    @Column(name = "old_address")
    private String oldAddress;
}
