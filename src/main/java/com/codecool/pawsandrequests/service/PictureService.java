package com.codecool.pawsandrequests.service;

import com.codecool.pawsandrequests.model.Picture;
import com.codecool.pawsandrequests.repository.PictureRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

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

    public Picture createPicture(final MultipartFile file) {
        Picture picture = new Picture();
        try {
            picture.setData(file.getBytes());
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Couldnt read picture");
        }
        picture.setContentType(file.getContentType());
        return pictureRepository.save(picture);

    }

    public void deletePicture(final Long id) {
        try {
            pictureRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(
            HttpStatus.CONFLICT, "Picture with id " + id + " already exists");
        }
    }
}
