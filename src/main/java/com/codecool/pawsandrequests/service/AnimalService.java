package com.codecool.pawsandrequests.service;

import com.codecool.pawsandrequests.dto.AnimalResponse;
import com.codecool.pawsandrequests.repository.AnimalRepository;
import com.codecool.pawsandrequests.repository.ShelterRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final ShelterRepository shelterRepository;

    public AnimalService(
            final AnimalRepository animalRepo,
            final ShelterRepository shelterRepo
    ) {
        this.animalRepository = animalRepo;
        this.shelterRepository = shelterRepo;
    }

    public final List<AnimalResponse> getMyAnimals(final String orgNr) {


        return animalRepository.findAll().stream()
                .filter(animal -> animal.getShelter()
                        .equals(shelterRepository.findByOrgNr(orgNr).get())
                )
                .map(animal -> new AnimalResponse(
                        animal.getName(),
                        animal.getAge(),
                        animal.getGender(),
                        animal.getSpecies()
                ))
                .toList();
    }
}
