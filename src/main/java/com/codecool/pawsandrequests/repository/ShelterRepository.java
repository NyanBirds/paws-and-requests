package com.codecool.pawsandrequests.repository;

import com.codecool.pawsandrequests.model.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShelterRepository extends JpaRepository<Shelter, String> {

    Optional<Shelter> findByOrgNr(String orgNr);
}
