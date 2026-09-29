package com.codecool.pawsandrequests.repository;

import com.codecool.pawsandrequests.model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, UUID> {

    List<Animal> findByShelterOrgNr(String orgNr);
}
