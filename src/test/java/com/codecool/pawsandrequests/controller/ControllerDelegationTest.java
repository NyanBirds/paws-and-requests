package com.codecool.pawsandrequests.controller;

import com.codecool.pawsandrequests.dto.AnimalRequest;
import com.codecool.pawsandrequests.dto.AnimalResponse;
import com.codecool.pawsandrequests.dto.ShelterResponse;
import com.codecool.pawsandrequests.model.CustomUserDetails;
import com.codecool.pawsandrequests.model.Gender;
import com.codecool.pawsandrequests.model.Picture;
import com.codecool.pawsandrequests.model.Species;
import com.codecool.pawsandrequests.service.AnimalService;
import com.codecool.pawsandrequests.service.PictureService;
import com.codecool.pawsandrequests.service.ShelterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.codecool.pawsandrequests.TestFixtures.ORG_NR;
import static com.codecool.pawsandrequests.TestFixtures.animal;
import static com.codecool.pawsandrequests.TestFixtures.details;
import static com.codecool.pawsandrequests.TestFixtures.picture;
import static com.codecool.pawsandrequests.TestFixtures.shelter;
import static com.codecool.pawsandrequests.TestFixtures.shelterUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Controller delegation")
class ControllerDelegationTest {

    @Mock
    private AnimalService animalService;

    @Mock
    private ShelterService shelterService;

    @Mock
    private PictureService pictureService;

    @Nested
    @DisplayName("AnimalController")
    class Animals {

        private AnimalController controller;

        @BeforeEach
        void setUp() {
            controller = new AnimalController(animalService);
        }

        @Test
        @DisplayName("scopes the listing to the principal's shelter")
        void scopesToPrincipalShelter() {
            CustomUserDetails principal = details(shelterUser(shelter()));
            AnimalResponse response = new AnimalResponse(UUID.randomUUID(),
                    "Maja", 3, Gender.FEMALE, Species.CAT);
            when(animalService.getMyAnimals(ORG_NR))
                    .thenReturn(List.of(response));

            assertThat(controller.getMyAnimals(principal))
                    .containsExactly(response);
            verify(animalService).getMyAnimals(ORG_NR);
        }

        @Test
        @DisplayName("adds an animal to the principal's shelter")
        void addsToPrincipalShelter() {
            CustomUserDetails principal = details(shelterUser(shelter()));
            AnimalRequest request = new AnimalRequest("Fod", 1, "MALE",
                    "DOG");

            controller.addAnimal(principal, request);

            verify(animalService).addAnimal(ORG_NR, request);
        }

        @Test
        @DisplayName("removes an animal from the principal's shelter")
        void removesFromPrincipalShelter() {
            CustomUserDetails principal = details(shelterUser(shelter()));
            UUID id = UUID.randomUUID();

            controller.removeAnimal(principal, id);

            verify(animalService).removeAnimal(ORG_NR, id);
        }

        @Test
        @DisplayName("looks up a single animal by id")
        void looksUpSingleAnimal() {
            UUID id = UUID.randomUUID();
            AnimalResponse response = new AnimalResponse(id, "Maja", 3,
                    Gender.FEMALE, Species.CAT);
            when(animalService.getAnimal(id)).thenReturn(response);

            assertThat(controller.getAnimal(id)).isSameAs(response);
        }
    }

    @Nested
    @DisplayName("ShelterController")
    class Shelters {

        private ShelterController controller;

        @BeforeEach
        void setUp() {
            controller = new ShelterController(shelterService);
        }

        @Test
        @DisplayName("returns every shelter")
        void returnsAllShelters() {
            ShelterResponse response = new ShelterResponse(ORG_NR, "Nyan",
                    "Rainbow Road 1", "We help", Optional.empty());
            when(shelterService.getAllShelters())
                    .thenReturn(List.of(response));

            assertThat(controller.getShelters())
                    .containsExactly(response);
        }

        @Test
        @DisplayName("returns one shelter by org number")
        void returnsOneShelter() {
            ShelterResponse response = new ShelterResponse(ORG_NR, "Nyan",
                    "Rainbow Road 1", "We help", Optional.empty());
            when(shelterService.getShelter(ORG_NR)).thenReturn(response);

            assertThat(controller.getShelter(ORG_NR)).isSameAs(response);
        }

        @Test
        @DisplayName("propagates a 404 for an unknown shelter")
        void propagatesNotFound() {
            when(shelterService.getShelter("missing")).thenThrow(
                    new ResponseStatusException(HttpStatus.NOT_FOUND)
            );

            assertThatThrownBy(() -> controller.getShelter("missing"))
                    .isInstanceOf(ResponseStatusException.class);
        }
    }

    @Nested
    @DisplayName("PictureController")
    class Pictures {

        private PictureController controller;

        @BeforeEach
        void setUp() {
            controller = new PictureController(pictureService);
        }

        @Test
        @DisplayName("returns the raw bytes with the stored content type")
        void returnsBytesWithContentType() {
            Picture stored = picture(3L);
            stored.setData("abc".getBytes(StandardCharsets.UTF_8));
            when(pictureService.getPicture(3L)).thenReturn(stored);

            ResponseEntity<byte[]> response = controller.getPicture(3L);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getHeaders().getContentType())
                    .isEqualTo(MediaType.IMAGE_JPEG);
            assertThat(response.getBody())
                    .containsExactly('a', 'b', 'c');
        }

        @Test
        @DisplayName("returns the new picture id after an upload")
        void returnsNewPictureId() {
            MultipartFile file = mock(
                    MultipartFile.class
            );
            Picture saved = picture(8L);
            when(pictureService.createPicture(file)).thenReturn(saved);

            ResponseEntity<Long> response = controller.savePicture(file);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(8L);
        }

        @Test
        @DisplayName("propagates a 404 for an unknown picture")
        void propagatesNotFound() {
            when(pictureService.getPicture(9L)).thenThrow(
                    new ResponseStatusException(HttpStatus.NOT_FOUND)
            );

            assertThatThrownBy(() -> controller.getPicture(9L))
                    .isInstanceOf(ResponseStatusException.class);
        }
    }

    @Nested
    @DisplayName("UserController")
    class Users {

        @Mock
        private com.codecool.pawsandrequests.service.UserService userService;

        @Test
        @DisplayName("edits using the caller's own email, not the path id")
        void editsUsingCallerEmail() {
            UserController controller = new UserController(userService);
            CustomUserDetails principal = details(shelterUser(shelter()));
            UUID pathId = UUID.randomUUID();
            com.codecool.pawsandrequests.dto.UserRequest request =
                    new com.codecool.pawsandrequests.dto.UserRequest(null,
                            "070-0000000");

            ResponseEntity<Void> response = controller.editUser(principal,
                    pathId, request);

            assertThat(response.getStatusCode())
                    .isEqualTo(HttpStatus.NO_CONTENT);
            verify(userService).editUser(principal.getUsername(), pathId,
                    request);
        }
    }

    @Nested
    @DisplayName("AdoptionFormController")
    class AdoptionForms {

        @Mock
        private com.codecool.pawsandrequests.service.AdoptionFormService
                adoptionFormService;

        @Test
        @DisplayName("returns 204 after creating a form")
        void returnsNoContentAfterCreate() {
            AdoptionFormController controller =
                    new AdoptionFormController(adoptionFormService);
            CustomUserDetails principal = details(shelterUser(shelter()));
            UUID postId = UUID.randomUUID();
            com.codecool.pawsandrequests.dto.AdoptionFormRequest request =
                    new com.codecool.pawsandrequests.dto.AdoptionFormRequest(
                            "Hi");

            ResponseEntity<Void> response = controller.createForm(principal,
                    postId, request);

            assertThat(response.getStatusCode())
                    .isEqualTo(HttpStatus.NO_CONTENT);
            verify(adoptionFormService).createForm(principal.getUsername(),
                    postId, request);
        }

        @Test
        @DisplayName("scopes the post's forms to the caller's email")
        void scopesGetFormsToCaller() {
            AdoptionFormController controller =
                    new AdoptionFormController(adoptionFormService);
            CustomUserDetails principal = details(shelterUser(shelter()));
            UUID postId = UUID.randomUUID();
            when(adoptionFormService.getForms(principal.getUsername(), postId))
                    .thenReturn(List.of());

            assertThat(controller.getForms(principal, postId)).isEmpty();
        }
    }
}
