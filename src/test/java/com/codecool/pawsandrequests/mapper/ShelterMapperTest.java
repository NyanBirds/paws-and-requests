package com.codecool.pawsandrequests.mapper;

import com.codecool.pawsandrequests.dto.ShelterResponse;
import com.codecool.pawsandrequests.model.Shelter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.codecool.pawsandrequests.TestFixtures.ORG_NR;
import static com.codecool.pawsandrequests.TestFixtures.picture;
import static com.codecool.pawsandrequests.TestFixtures.shelter;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ShelterMapper")
class ShelterMapperTest {

    private ShelterMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ShelterMapperImpl();
    }

    @Test
    @DisplayName("copies the shelter fields")
    void copiesFields() {
        Shelter shelter = shelter();

        ShelterResponse result = mapper.toShelterInfo(shelter);

        assertThat(result.orgNr()).isEqualTo(ORG_NR);
        assertThat(result.shelterName()).isEqualTo(shelter.getShelterName());
        assertThat(result.address()).isEqualTo(shelter.getAddress());
        assertThat(result.description()).isEqualTo(shelter.getDescription());
    }

    @Test
    @DisplayName("builds the picture url from the picture id")
    void buildsPictureUrl() {
        Shelter shelter = shelter();
        shelter.setPicture(picture(17L));

        assertThat(mapper.toShelterInfo(shelter).url())
                .contains("/pictures/17");
    }

    @Test
    @DisplayName("has an empty url when the shelter has no picture")
    void emptyUrlWithoutPicture() {
        assertThat(mapper.toShelterInfo(shelter()).url())
                .isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("returns null for a null shelter")
    void nullInNullOut() {
        assertThat(mapper.toShelterInfo(null)).isNull();
    }

    @Test
    @DisplayName("pictureUrl is a named helper usable on its own")
    void pictureUrlHelper() {
        Shelter withPicture = shelter();
        withPicture.setPicture(picture(3L));

        assertThat(mapper.pictureUrl(withPicture))
                .contains("/pictures/3");
        assertThat(mapper.pictureUrl(shelter())).isEmpty();
    }
}
