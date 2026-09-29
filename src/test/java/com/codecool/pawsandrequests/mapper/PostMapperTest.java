package com.codecool.pawsandrequests.mapper;

import com.codecool.pawsandrequests.dto.PostResponse;
import com.codecool.pawsandrequests.dto.PostSummaryResponse;
import com.codecool.pawsandrequests.model.Animal;
import com.codecool.pawsandrequests.model.Gender;
import com.codecool.pawsandrequests.model.Role;
import com.codecool.pawsandrequests.model.Picture;
import com.codecool.pawsandrequests.model.Post;
import com.codecool.pawsandrequests.model.Shelter;
import com.codecool.pawsandrequests.model.Species;
import com.codecool.pawsandrequests.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.codecool.pawsandrequests.TestFixtures.OTHER_ORG_NR;
import static com.codecool.pawsandrequests.TestFixtures.animal;
import static com.codecool.pawsandrequests.TestFixtures.picture;
import static com.codecool.pawsandrequests.TestFixtures.post;
import static com.codecool.pawsandrequests.TestFixtures.shelter;
import static com.codecool.pawsandrequests.TestFixtures.user;
import static com.codecool.pawsandrequests.TestFixtures.shelterUser;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("PostMapper")
class PostMapperTest {

    private PostMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new PostMapperImpl();
    }

    @Nested
    @DisplayName("toPostSummaryResponse")
    class ToPostSummaryResponse {

        @Test
        @DisplayName("copies the post fields and flattens the animal")
        void copiesFields() {
            Shelter shelter = shelter();
            Animal animal = animal(shelter, "Maja", 4, Gender.FEMALE,
                    Species.CAT);
            Post post = post(shelterUser(shelter), animal);

            PostSummaryResponse result = mapper.toPostSummaryResponse(post);

            assertThat(result.id()).isEqualTo(post.getId());
            assertThat(result.title()).isEqualTo(post.getTitle());
            assertThat(result.age()).isEqualTo(4);
            assertThat(result.gender()).isEqualTo(Gender.FEMALE);
            assertThat(result.species()).isEqualTo(Species.CAT);
        }

        @Test
        @DisplayName("uses the first picture as the cover url")
        void usesFirstPictureAsCover() {
            Post post = post(shelterUser(shelter()), animal(shelter()));
            post.getPictures().add(picture(1L));
            post.getPictures().add(picture(2L));

            assertThat(mapper.toPostSummaryResponse(post).url())
                    .contains("/pictures/1");
        }

        @Test
        @DisplayName("has no url when the post has no pictures")
        void noUrlWithoutPictures() {
            Post post = post(shelterUser(shelter()), animal(shelter()));

            assertThat(mapper.toPostSummaryResponse(post).url())
                    .isEqualTo(Optional.empty());
        }

        @Test
        @DisplayName("returns null for a null post")
        void nullInNullOut() {
            assertThat(mapper.toPostSummaryResponse(null)).isNull();
        }

        @Test
        @DisplayName("tolerates a missing animal")
        void toleratesMissingAnimal() {
            Post post = post(shelterUser(shelter()), null);

            PostSummaryResponse result = mapper.toPostSummaryResponse(post);

            assertThat(result.age()).isZero();
            assertThat(result.gender()).isNull();
            assertThat(result.species()).isNull();
        }
    }

    @Nested
    @DisplayName("toPostResponse")
    class ToPostResponse {

        @Test
        @DisplayName("joins the post, animal and shelter details")
        void joinsDetails() {
            Shelter shelter = shelter();
            Animal animal = animal(shelter, "Maja", 4, Gender.FEMALE,
                    Species.CAT);
            Post post = post(shelterUser(shelter), animal);

            PostResponse result = mapper.toPostResponse(post);

            assertThat(result.id()).isEqualTo(post.getId());
            assertThat(result.title()).isEqualTo(post.getTitle());
            assertThat(result.description()).isEqualTo(post.getDescription());
            assertThat(result.shelterName()).isEqualTo(shelter.getShelterName());
            assertThat(result.address()).isEqualTo(shelter.getAddress());
            assertThat(result.animalName()).isEqualTo("Maja");
            assertThat(result.age()).isEqualTo(4);
        }

        @Test
        @DisplayName("lists every picture url and id in order")
        void listsAllPictures() {
            Post post = post(shelterUser(shelter()), animal(shelter()));
            post.getPictures().add(picture(1L));
            post.getPictures().add(picture(2L));

            PostResponse result = mapper.toPostResponse(post);

            assertThat(result.url()).containsExactly("/pictures/1",
                    "/pictures/2");
            assertThat(result.pictureIds()).containsExactly(1L, 2L);
        }

        @Test
        @DisplayName("returns empty lists when there are no pictures")
        void emptyPictureLists() {
            Post post = post(shelterUser(shelter()), animal(shelter()));

            PostResponse result = mapper.toPostResponse(post);

            assertThat(result.url()).isEmpty();
            assertThat(result.pictureIds()).isEmpty();
        }

        @Test
        @DisplayName("leaves shelter fields null when the author has none")
        void toleratesAuthorWithoutShelter() {
            Post post = post(
                    user(
                            Role.USER,
                            null),
                    animal(shelter(OTHER_ORG_NR))
            );

            PostResponse result = mapper.toPostResponse(post);

            assertThat(result.shelterName()).isNull();
            assertThat(result.address()).isNull();
        }

        @Test
        @DisplayName("returns null for a null post")
        void nullInNullOut() {
            assertThat(mapper.toPostResponse(null)).isNull();
        }
    }

    @Nested
    @DisplayName("toPost")
    class ToPost {

        @Test
        @DisplayName("copies the request fields and links user and animal")
        void copiesAndLinks() {
            Animal animal = animal(shelter());
            User user = shelterUser(shelter());
            UUID animalId = animal.getId();

            Post result = mapper.toPost(
                    new com.codecool.pawsandrequests.dto.PostRequest("Title",
                            "Description", animalId),
                    user, animal
            );

            assertThat(result.getTitle()).isEqualTo("Title");
            assertThat(result.getDescription()).isEqualTo("Description");
            assertThat(result.getUser()).isSameAs(user);
            assertThat(result.getAnimal()).isSameAs(animal);
        }

        @Test
        @DisplayName("leaves the generated id unset")
        void leavesIdUnset() {
            Post result = mapper.toPost(
                    new com.codecool.pawsandrequests.dto.PostRequest("T", "D",
                            UUID.randomUUID()),
                    shelterUser(shelter()), animal(shelter())
            );

            assertThat(result.getId()).isNull();
        }

        @Test
        @DisplayName("starts with an empty picture list")
        void startsWithEmptyPictures() {
            Post result = mapper.toPost(
                    new com.codecool.pawsandrequests.dto.PostRequest("T", "D",
                            UUID.randomUUID()),
                    shelterUser(shelter()), animal(shelter())
            );

            assertThat(result.getPictures()).isEmpty();
        }

        @Test
        @DisplayName("does not copy the author's adoption forms onto the post")
        void doesNotCopyAdoptionForms() {
            User user = shelterUser(shelter());
            Post result = mapper.toPost(
                    new com.codecool.pawsandrequests.dto.PostRequest("T", "D",
                            UUID.randomUUID()),
                    user, animal(shelter())
            );

            assertThat(result.getAdoptionForms())
                    .isEqualTo(List.of());
        }

        @Test
        @DisplayName("returns null when every argument is null")
        void nullInNullOut() {
            assertThat(mapper.toPost(null, null, null)).isNull();
        }
    }

    @Test
    @DisplayName("picture urls are prefixed with the picture endpoint")
    void pictureUrlPrefix() {
        Post post = post(shelterUser(shelter()), animal(shelter()));
        Picture picture = picture(42L);
        post.getPictures().add(picture);

        assertThat(mapper.toPostSummaryResponse(post).url())
                .contains("/pictures/" + picture.getId());
    }
}
