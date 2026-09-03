package com.codecool.paws_and_requests.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Shelter {
    @Id
    private String orgNr;

    @Column(nullable = false)
    private String shelterName;
    private String address;
}
