package com.codecool.pawsandrequests.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Shelter {
    @Id
    private String orgNr;

    @Column(nullable = false)
    private String shelterName;
    private String address;

    @OneToMany(mappedBy = "shelter")
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "shelter")
    private List<Animal> animals = new ArrayList<>();
}
