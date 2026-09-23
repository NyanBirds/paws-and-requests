package com.codecool.pawsandrequests.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Shelter {
    public static final int INT = 1_000;
    @Id
    private String orgNr;

    @Column(nullable = false)
    private String shelterName;
    private String address;
    private String description;

    @Column(length = INT)
    private String logo;

    @OneToMany(mappedBy = "shelter")
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "shelter")
    private List<Animal> animals = new ArrayList<>();
}
