package com.codecool.pawsandrequests.controller;

import com.codecool.pawsandrequests.model.Picture;
import com.codecool.pawsandrequests.service.PictureService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pictures")
public final class PictureController {
    private final PictureService pictureService;
    public PictureController(final PictureService ps) {
        this.pictureService = ps;
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getPicture(
            @PathVariable final Long id) {
        Picture picture = pictureService.getPicture(id);
        return ResponseEntity.ok().contentType(
                MediaType.parseMediaType(picture.getContentType()))
                .body(picture.getData());
    }

}
