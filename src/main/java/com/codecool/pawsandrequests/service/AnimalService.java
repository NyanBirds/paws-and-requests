package com.codecool.pawsandrequests.service;

import com.codecool.pawsandrequests.dto.AnimalRequest;
import com.codecool.pawsandrequests.dto.AnimalResponse;
import com.codecool.pawsandrequests.mapper.AnimalMapper;
import com.codecool.pawsandrequests.model.Animal;
import com.codecool.pawsandrequests.model.Shelter;
import com.codecool.pawsandrequests.repository.AnimalRepository;
import com.codecool.pawsandrequests.repository.ShelterRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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
                        .equals(shelterRepository.findByOrgNr(orgNr).get())
                )
                .map(animalMapper::toAnimalResponse)
                .toList();
    }

    public final AnimalResponse getAnimal(final UUID id) {
        Animal animal = animalRepository.findById(id).get();
        return animalMapper.toAnimalResponse(animal);
    }

    public final void addAnimal(
            final String orgNr,
            final AnimalRequest request
    ) {
        Shelter shelter = shelterRepository.findByOrgNr(orgNr).get();
        Animal animal = animalMapper.toAnimal(request, shelter);
        animalRepository.save(animal);
    }

    public final void removeAnimal(
            final String orgNr,
            final UUID id
    ) {
        shelterRepository.findByOrgNr(orgNr).get().getAnimals().stream()
                .filter(animal -> animal.getId().equals(id))
                .findFirst().ifPresent(animalRepository::delete);
    }
}
