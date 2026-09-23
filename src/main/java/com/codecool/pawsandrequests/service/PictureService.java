package com.codecool.pawsandrequests.service;

import com.codecool.pawsandrequests.model.Picture;
import com.codecool.pawsandrequests.repository.PictureRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public final class PictureService {

    private final PictureRepository pictureRepository;

    public PictureService(final PictureRepository pr) {
        this.pictureRepository = pr;
    }

    public Picture getPicture(final Long id) {
        return pictureRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Couldnt find picture"));
    }
}
