package com.codecool.pawsandrequests.repository;

import com.codecool.pawsandrequests.model.Gender;
import com.codecool.pawsandrequests.model.Post;
import com.codecool.pawsandrequests.model.Species;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {

    List<Post> findByAnimalGenderIn(List<Gender> gender);
    List<Post> findByAnimalSpeciesIn(List<Species> species);
    List<Post> findByAnimalGenderInAndAnimalSpeciesIn(
            List<Gender> gender,
            List<Species> species
    );
}
