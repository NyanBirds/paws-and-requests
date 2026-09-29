package com.codecool.pawsandrequests.service;

import com.codecool.pawsandrequests.dto.AnimalRequest;
import com.codecool.pawsandrequests.dto.AnimalResponse;
import com.codecool.pawsandrequests.mapper.AnimalMapper;
import com.codecool.pawsandrequests.model.Animal;
import com.codecool.pawsandrequests.model.Gender;
import com.codecool.pawsandrequests.model.Shelter;
import com.codecool.pawsandrequests.model.Species;
import com.codecool.pawsandrequests.repository.AnimalRepository;
import com.codecool.pawsandrequests.repository.ShelterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static com.codecool.pawsandrequests.TestFixtures.ORG_NR;
import static com.codecool.pawsandrequests.TestFixtures.OTHER_ORG_NR;
import static com.codecool.pawsandrequests.TestFixtures.animal;
import static com.codecool.pawsandrequests.TestFixtures.shelter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("AnimalService")
class AnimalServiceTest {

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private ShelterRepository shelterRepository;

    @Mock
    private AnimalMapper animalMapper;

    private AnimalService animalService;

    @BeforeEach
    void setUp() {
        animalService = new AnimalService(animalRepository,
                shelterRepository, animalMapper);
    }

    @Test
    @DisplayName("returns only the animals belonging to the shelter")
    void filtersByShelter() {
        Shelter mine = shelter();
        Animal own = animal(mine);
        Animal foreign = animal(shelter(OTHER_ORG_NR));
        when(animalRepository.findAll()).thenReturn(List.of(own, foreign));
        when(shelterRepository.findByOrgNr(ORG_NR))
                .thenReturn(Optional.of(mine));
        when(animalMapper.toAnimalResponse(own))
                .thenReturn(new AnimalResponse(own.getId(), own.getName(),
                        own.getAge(), own.getGender(), own.getSpecies()));

        List<AnimalResponse> result = animalService.getMyAnimals(ORG_NR);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().name()).isEqualTo("Maja");
    }

        @Test
        @DisplayName("returns an empty list when the shelter has no animals")
        void returnsEmptyList() {
            when(animalRepository.findAll()).thenReturn(List.of());

            assertThat(animalService.getMyAnimals(ORG_NR)).isEmpty();
        }

    @Test
    @DisplayName("maps a single animal by id")
    void mapsSingleAnimal() {
        Animal target = animal(shelter());
        when(animalRepository.findById(target.getId()))
                .thenReturn(Optional.of(target));
        when(animalMapper.toAnimalResponse(target)).thenReturn(
                new AnimalResponse(target.getId(), "Maja", 3, Gender.FEMALE,
                        Species.CAT)
        );

        AnimalResponse result = animalService.getAnimal(target.getId());

        assertThat(result.id()).isEqualTo(target.getId());
        assertThat(result.species()).isEqualTo(Species.CAT);
    }

    @Test
    @DisplayName("fails when the animal does not exist")
    void failsForUnknownAnimal() {
        UUID missing = UUID.randomUUID();
        when(animalRepository.findById(missing)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> animalService.getAnimal(missing))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("maps a new animal onto the caller's shelter and saves it")
    void addsAnimal() {
        Shelter mine = shelter();
        when(shelterRepository.findByOrgNr(ORG_NR))
                .thenReturn(Optional.of(mine));
        Animal mapped = animal(mine, "Fod", 1, Gender.MALE, Species.DOG);
        when(animalMapper.toAnimal(any(AnimalRequest.class),
                eq(mine))).thenReturn(mapped);

        animalService.addAnimal(ORG_NR,
                new AnimalRequest("Fod", 1, "MALE", "DOG"));

        verify(animalRepository).save(mapped);
    }

    @Test
    @DisplayName("fails when adding to an unknown shelter")
    void failsForUnknownShelter() {
        when(shelterRepository.findByOrgNr(ORG_NR))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> animalService.addAnimal(ORG_NR,
                new AnimalRequest("Fod", 1, "MALE", "DOG")))
                .isInstanceOf(NoSuchElementException.class);

        verify(animalRepository, never()).save(any());
    }

    @Test
    @DisplayName("deletes an animal owned by the shelter")
    void removesOwnAnimal() {
        Shelter mine = shelter();
        Animal own = animal(mine);
        mine.getAnimals().add(own);
        when(shelterRepository.findByOrgNr(ORG_NR))
                .thenReturn(Optional.of(mine));

        animalService.removeAnimal(ORG_NR, own.getId());

        verify(animalRepository).delete(own);
    }

    @Test
    @DisplayName("ignores an id that the shelter does not own")
    void ignoresForeignAnimal() {
        Shelter mine = shelter();
        mine.getAnimals().add(animal(mine));
        when(shelterRepository.findByOrgNr(ORG_NR))
                .thenReturn(Optional.of(mine));

        animalService.removeAnimal(ORG_NR, UUID.randomUUID());

        verify(animalRepository, never()).delete(any());
    }

    @Test
    @DisplayName("only deletes the first match")
    void deletesFirstMatchOnly() {
        Shelter mine = shelter();
        Animal own = animal(mine);
        mine.getAnimals().add(own);
        mine.getAnimals().add(own);
        when(shelterRepository.findByOrgNr(ORG_NR))
                .thenReturn(Optional.of(mine));

        animalService.removeAnimal(ORG_NR, own.getId());

        ArgumentCaptor<Animal> captor = ArgumentCaptor.forClass(
                Animal.class
        );
        verify(animalRepository, times(1))
                .delete(captor.capture());
        assertThat(captor.getValue()).isSameAs(own);
    }
}
