package com.codecool.pawsandrequests.service;

import com.codecool.pawsandrequests.dto.ShelterRequest;
import com.codecool.pawsandrequests.dto.ShelterResponse;
import com.codecool.pawsandrequests.exception.ShelterNotFoundException;
import com.codecool.pawsandrequests.mapper.ShelterMapper;
import com.codecool.pawsandrequests.model.Animal;
import com.codecool.pawsandrequests.model.Picture;
import com.codecool.pawsandrequests.model.Role;
import com.codecool.pawsandrequests.model.Shelter;
import com.codecool.pawsandrequests.model.User;
import com.codecool.pawsandrequests.repository.AnimalRepository;
import com.codecool.pawsandrequests.repository.ShelterRepository;
import com.codecool.pawsandrequests.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public final class ShelterService {
    private final ShelterRepository repository;
    private final ShelterMapper mapper;
    private final UserRepository userRepository;
    private final AnimalRepository animalRepository;
    private final PictureService pictureService;

    public ShelterService(
            final ShelterRepository r,
            final ShelterMapper m,
            final UserRepository ur,
            final AnimalRepository ar,
            final PictureService ps) {
        repository = r;
        mapper = m;
        userRepository = ur;
        animalRepository = ar;
        pictureService = ps;
    }

    public List<ShelterResponse> getAllShelters() {
        List<Shelter> shelters = repository.findAll();
        return shelters.stream().map(mapper::toShelterInfo).toList();
    }

    public ShelterResponse getShelter(final String orgNr) {
        return mapper.toShelterInfo(findShelter(orgNr));
    }

    public ShelterResponse createShelter(
            final ShelterRequest request,
            final MultipartFile pictureFile,
            final String requesterEmail) {
        requireAdmin(requesterEmail);

        if (isBlank(request.orgNr())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "org nr is required");
        }
        if (isBlank(request.shelterName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "shelter name is required");
        }
        if (repository.existsByOrgNr(request.orgNr())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "shelter with org nr " + request.orgNr()
                            + " already exists");
        }

        Shelter shelter = mapper.toShelter(request);
        shelter.setPicture(toPicture(pictureFile));

        Shelter saved = repository.save(shelter);
        return mapper.toShelterInfo(saved);
    }

    public ShelterResponse updateShelter(
            final String orgNr,
            final ShelterRequest request,
            final MultipartFile pictureFile,
            final String requesterEmail) {
        requireAdminOrOwnShelter(requesterEmail, orgNr);

        Shelter shelter = findShelter(orgNr);

        if (request.shelterName() != null) {
            shelter.setShelterName(request.shelterName());
        }
        if (request.address() != null) {
            shelter.setAddress(request.address());
        }
        if (request.description() != null) {
            shelter.setDescription(request.description());
        }
        if (hasContent(pictureFile)) {
            shelter.setPicture(toPicture(pictureFile));
        }

        return mapper.toShelterInfo(repository.save(shelter));
    }

    public void deleteShelter(
            final String orgNr,
            final String requesterEmail) {
        requireAdmin(requesterEmail);
        Shelter shelter = findShelter(orgNr);

        List<User> users = userRepository.findByShelterOrgNr(orgNr);
        for (User user : users) {
            user.setRole(Role.USER);
            user.setShelter(null);
        }
        userRepository.saveAll(users);

        List<Animal> animals = animalRepository.findByShelterOrgNr(orgNr);
        animalRepository.deleteAll(animals);

        repository.delete(shelter);
    }

    private Shelter findShelter(final String orgNr) {
        return repository.findByOrgNr(orgNr)
                .orElseThrow(() -> new ShelterNotFoundException(orgNr));
    }

    private User findRequester(final String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "user not found"));
    }

    private void requireAdmin(final String requesterEmail) {
        if (findRequester(requesterEmail).getRole() != Role.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Only admins can manage shelters");
        }
    }

    private void requireAdminOrOwnShelter(
            final String requesterEmail,
            final String orgNr) {
        User user = findRequester(requesterEmail);

        if (user.getRole() == Role.ADMIN) {
            return;
        }
        if (user.getRole() == Role.SHELTERUSER
                && user.getShelter() != null
                && orgNr.equals(user.getShelter().getOrgNr())) {
            return;
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                "You can only edit your own shelter");
    }

    private boolean isBlank(final String value) {
        return value == null || value.isBlank();
    }

    private boolean hasContent(final MultipartFile file) {
        return file != null && !file.isEmpty();
    }

    private Picture toPicture(final MultipartFile file) {
        if (!hasContent(file)) {
            return null;
        }
        return pictureService.createPicture(file);
    }
}
