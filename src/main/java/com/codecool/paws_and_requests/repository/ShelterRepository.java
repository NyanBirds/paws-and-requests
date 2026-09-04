package com.codecool.paws_and_requests.repository;

import com.codecool.paws_and_requests.model.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShelterRepository extends JpaRepository<Shelter, String> {
}
