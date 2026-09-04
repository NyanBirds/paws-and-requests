package com.codecool.pawsandrequests.repository;

import com.codecool.pawsandrequests.model.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShelterRepository extends JpaRepository<Shelter, String> {
}
