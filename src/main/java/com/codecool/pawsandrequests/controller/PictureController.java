package com.codecool.pawsandrequests.controller;

import com.codecool.pawsandrequests.model.Picture;
import com.codecool.pawsandrequests.service.PictureService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

   @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> savePicture(
            @RequestParam final MultipartFile file
   ) {
        Picture picture = pictureService.createPicture(file);
        return ResponseEntity.ok(picture.getId());
   }


}
