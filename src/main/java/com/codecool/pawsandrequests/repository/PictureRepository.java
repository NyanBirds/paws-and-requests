package com.codecool.pawsandrequests.repository;

import com.codecool.pawsandrequests.model.Picture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PictureRepository extends JpaRepository<Picture, Long> {
}
