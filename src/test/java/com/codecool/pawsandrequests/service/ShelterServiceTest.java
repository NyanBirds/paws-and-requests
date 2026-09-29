package com.codecool.pawsandrequests.service;

import com.codecool.pawsandrequests.dto.ShelterResponse;
import com.codecool.pawsandrequests.mapper.ShelterMapper;
import com.codecool.pawsandrequests.model.Shelter;
import com.codecool.pawsandrequests.repository.ShelterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static com.codecool.pawsandrequests.TestFixtures.ORG_NR;
import static com.codecool.pawsandrequests.TestFixtures.OTHER_ORG_NR;
import static com.codecool.pawsandrequests.TestFixtures.picture;
import static com.codecool.pawsandrequests.TestFixtures.shelter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ShelterService")
class ShelterServiceTest {

    @Mock
    private ShelterRepository repository;

    @Mock
    private ShelterMapper mapper;

    private ShelterService shelterService;

    @BeforeEach
    void setUp() {
        shelterService = new ShelterService(repository, mapper);
    }

    @Test
    @DisplayName("maps every shelter from the repository")
    void mapsAllShelters() {
        Shelter first = shelter();
        Shelter second = shelter(OTHER_ORG_NR);
        when(repository.findAll()).thenReturn(List.of(first, second));
        when(mapper.toShelterInfo(first)).thenReturn(new ShelterResponse(
                first.getOrgNr(), first.getShelterName(), first.getAddress(),
                first.getDescription(), Optional.empty()
        ));
        when(mapper.toShelterInfo(second)).thenReturn(new ShelterResponse(
                second.getOrgNr(), second.getShelterName(),
                second.getAddress(), second.getDescription(),
                Optional.empty()
        ));

        List<ShelterResponse> result = shelterService.getAllShelters();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(ShelterResponse::orgNr)
                .containsExactly(ORG_NR, OTHER_ORG_NR);
    }

    @Test
    @DisplayName("returns an empty list when there are no shelters")
    void returnsEmptyList() {
        when(repository.findAll()).thenReturn(List.of());

        assertThat(shelterService.getAllShelters()).isEmpty();
    }

    @Test
    @DisplayName("maps a shelter looked up by org number")
    void mapsShelterByOrgNumber() {
        Shelter found = shelter();
        when(repository.findByOrgNr(ORG_NR)).thenReturn(Optional.of(found));
        when(mapper.toShelterInfo(found)).thenReturn(new ShelterResponse(
                ORG_NR, found.getShelterName(), found.getAddress(),
                found.getDescription(), Optional.of(
                "/pictures/7")
        ));

        ShelterResponse result = shelterService.getShelter(ORG_NR);

        assertThat(result.orgNr()).isEqualTo(ORG_NR);
        assertThat(result.url()).contains("/pictures/7");
    }

    @Test
    @DisplayName("throws 404 for an unknown org number")
    void throwsForUnknownOrgNumber() {
        when(repository.findByOrgNr(ORG_NR)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> shelterService.getShelter(ORG_NR))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(e -> assertThat(
                        ((ResponseStatusException) e).getStatusCode()
                ).isEqualTo(HttpStatus.NOT_FOUND));

        verify(mapper, never()).toShelterInfo(org.mockito.ArgumentMatchers
                .any());
    }

    @Test
    @DisplayName("passes the picture url through the mapper")
    void pictureUrlGoesThroughMapper() {
        Shelter withPicture = shelter();
        withPicture.setPicture(picture(3L));
        when(repository.findByOrgNr(ORG_NR))
                .thenReturn(Optional.of(withPicture));
        when(mapper.toShelterInfo(withPicture)).thenReturn(
                new ShelterResponse(ORG_NR, withPicture.getShelterName(),
                        withPicture.getAddress(), withPicture.getDescription(),
                        Optional.of("/pictures/3"))
        );

        assertThat(shelterService.getShelter(ORG_NR).url())
                .contains("/pictures/3");

        verify(repository).findByOrgNr(ORG_NR);
    }
}
