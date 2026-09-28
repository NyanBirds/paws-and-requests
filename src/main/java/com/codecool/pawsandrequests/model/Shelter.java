package com.codecool.pawsandrequests.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
    private String description;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true)
    @JoinColumn(name = "picture_id")
    private Picture picture;

    @OneToMany(mappedBy = "shelter")
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "shelter")
    private List<Animal> animals = new ArrayList<>();
}
