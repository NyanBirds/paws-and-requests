package com.codecool.pawsandrequests.service;

import com.codecool.pawsandrequests.dto.ShelterResponse;
import com.codecool.pawsandrequests.mapper.ShelterMapper;
import com.codecool.pawsandrequests.model.Shelter;
import com.codecool.pawsandrequests.repository.ShelterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
public final class ShelterService {
    private final ShelterRepository repository;
    private final ShelterMapper mapper;

    public ShelterService(final ShelterRepository r, final ShelterMapper m) {
        repository = r;
        mapper = m;
    }

    public ShelterResponse getShelter(final String orgNr) {
        log.info("service");
        Shelter shelter = repository.findByOrgNr(orgNr)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "post not found")
                );

        return mapper.toShelterInfo(shelter);
    }
}
