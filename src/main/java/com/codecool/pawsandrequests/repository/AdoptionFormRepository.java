package com.codecool.pawsandrequests.repository;

import com.codecool.pawsandrequests.model.AdoptionForm;
import com.codecool.pawsandrequests.model.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AdoptionFormRepository extends
        JpaRepository<AdoptionForm, UUID> {
    List<AdoptionForm> findByPostUserShelterOrgNr(String userShelterOrgNr);
}
