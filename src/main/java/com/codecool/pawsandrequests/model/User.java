package com.codecool.pawsandrequests.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String email;
    private String phoneNumber;
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    @OneToOne
    @JoinColumn(name = "picture_id")
    private Picture picture;
    @ManyToOne
    @JoinColumn(name = "org_nr")
    private Shelter shelter;
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<AdoptionForm> adoptionForms = new ArrayList<>();
}
