package com.codecool.pawsandrequests.mapper;

import com.codecool.pawsandrequests.dto.AnimalRequest;
import com.codecool.pawsandrequests.dto.AnimalResponse;
import com.codecool.pawsandrequests.model.Animal;
import com.codecool.pawsandrequests.model.Gender;
import com.codecool.pawsandrequests.model.Shelter;
import com.codecool.pawsandrequests.model.Species;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codecool.pawsandrequests.TestFixtures.ORG_NR;
import static com.codecool.pawsandrequests.TestFixtures.animal;
import static com.codecool.pawsandrequests.TestFixtures.shelter;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AnimalMapper")
class AnimalMapperTest {

    private AnimalMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new AnimalMapperImpl();
    }

    @Test
    @DisplayName("maps an animal to a response")
    void mapsToResponse() {
        Animal animal = animal(shelter(), "Maja", 2, Gender.FEMALE,
                Species.CAT);

        AnimalResponse result = mapper.toAnimalResponse(animal);

        assertThat(result.id()).isEqualTo(animal.getId());
        assertThat(result.name()).isEqualTo("Maja");
        assertThat(result.age()).isEqualTo(2);
        assertThat(result.gender()).isEqualTo(Gender.FEMALE);
        assertThat(result.species()).isEqualTo(Species.CAT);
    }

    @Test
    @DisplayName("does not leak the shelter into the response")
    void hidesShelter() {
        AnimalResponse result = mapper.toAnimalResponse(animal(shelter()));

        assertThat(result.species()).isNotNull();
        assertThat(result.toString()).doesNotContain(ORG_NR);
    }

    @Test
    @DisplayName("returns null for a null animal")
    void nullInNullOut() {
        assertThat(mapper.toAnimalResponse(null)).isNull();
    }

    @Test
    @DisplayName("maps a request onto a new animal owned by the shelter")
    void mapsRequestToAnimal() {
        Shelter shelter = shelter();

        Animal result = mapper.toAnimal(
                new AnimalRequest("Fod", 1, "MALE", "DOG"), shelter
        );

        assertThat(result.getName()).isEqualTo("Fod");
        assertThat(result.getAge()).isEqualTo(1);
        assertThat(result.getGender()).isEqualTo(Gender.MALE);
        assertThat(result.getSpecies()).isEqualTo(Species.DOG);
        assertThat(result.getShelter()).isSameAs(shelter);
    }

    @Test
    @DisplayName("leaves the generated id unset")
    void leavesIdUnset() {
        Animal result = mapper.toAnimal(
                new AnimalRequest("Fod", 1, "MALE", "DOG"), shelter()
        );

        assertThat(result.getId()).isNull();
    }

    @Test
    @DisplayName("converts the gender and species strings to enums")
    void convertsEnumNames() {
        Animal result = mapper.toAnimal(
                new AnimalRequest("Bo", 5, "FEMALE", "CAT"), shelter()
        );

        assertThat(result.getGender()).isEqualTo(Gender.FEMALE);
        assertThat(result.getSpecies()).isEqualTo(Species.CAT);
    }

    @Test
    @DisplayName("fails on an unknown species name")
    void failsOnUnknownSpecies() {
        assertThat(
                org.assertj.core.api.Assertions.catchThrowable(() ->
                        mapper.toAnimal(new AnimalRequest("Bo", 5, "MALE",
                                "DRAGON"), shelter()))
        ).isNotNull();
    }
}
