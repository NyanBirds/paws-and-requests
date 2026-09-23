package com.codecool.pawsandrequests.controller;

import com.codecool.pawsandrequests.model.Picture;
import com.codecool.pawsandrequests.repository.PictureRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/pictures")
public class PictureController {
    private final PictureRepository pictureRepository;
    public PictureController(PictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getPicture(
            @PathVariable Long id){
        Picture picture = pictureRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Couldnt find picture"));
        return ResponseEntity.ok().contentType(
                MediaType.parseMediaType(picture.getContentType()))
                .body(picture.getData());
    }

}
