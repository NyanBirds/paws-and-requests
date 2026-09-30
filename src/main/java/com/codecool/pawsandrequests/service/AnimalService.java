package com.codecool.pawsandrequests.service;

import com.codecool.pawsandrequests.dto.AnimalRequest;
import com.codecool.pawsandrequests.dto.AnimalResponse;
import com.codecool.pawsandrequests.exception.ResourceNotFoundException;
import com.codecool.pawsandrequests.mapper.AnimalMapper;
import com.codecool.pawsandrequests.model.Animal;
import com.codecool.pawsandrequests.model.Shelter;
import com.codecool.pawsandrequests.repository.AnimalRepository;
import com.codecool.pawsandrequests.repository.ShelterRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final ShelterRepository shelterRepository;
    private final AnimalMapper animalMapper;

    public AnimalService(
            final AnimalRepository animalRepo,
            final ShelterRepository shelterRepo,
            final AnimalMapper mapper
    ) {
        this.animalRepository = animalRepo;
        this.shelterRepository = shelterRepo;
        this.animalMapper = mapper;
    }

    public final List<AnimalResponse> getMyAnimals(final String orgNr) {
        return animalRepository.findAll().stream()
                .filter(animal -> animal.getShelter()
                        .equals(shelterRepository.findByOrgNr(orgNr)
                                .orElseThrow(() -> new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Shelter not found")
                                )))
                .map(animalMapper::toAnimalResponse)
                .toList();
    }

    public final AnimalResponse getAnimal(final UUID id) {
        Animal animal = animalRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(id, "Animal")
                );
        return animalMapper.toAnimalResponse(animal);
    }

    public final AnimalResponse addAnimal(
            final String orgNr,
            final AnimalRequest request
    ) {
        Shelter shelter = shelterRepository.findByOrgNr(orgNr)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.
                        NOT_FOUND, "Shelter not found")
                );
        Animal animal = animalMapper.toAnimal(request, shelter);
        animalRepository.save(animal);
        return animalMapper.toAnimalResponse(animal);
    }

    public final void removeAnimal(
            final String orgNr,
            final UUID id
    ) {
        Shelter shelter = shelterRepository.findByOrgNr(orgNr)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.
                        NOT_FOUND, "Shelter not found")
                );

        shelter.getAnimals().stream()
                .filter(animal -> animal.getId().equals(id))
                .findFirst().ifPresent(animalRepository::delete);
    }
}
