package com.codecool.pawsandrequests.mapper;

import com.codecool.pawsandrequests.dto.AdoptionFormRequest;
import com.codecool.pawsandrequests.dto.AdoptionFormResponse;
import com.codecool.pawsandrequests.model.AdoptionForm;
import com.codecool.pawsandrequests.model.Animal;
import com.codecool.pawsandrequests.model.Post;
import com.codecool.pawsandrequests.model.Shelter;
import com.codecool.pawsandrequests.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codecool.pawsandrequests.TestFixtures.EMAIL;
import static com.codecool.pawsandrequests.TestFixtures.animal;
import static com.codecool.pawsandrequests.TestFixtures.post;
import static com.codecool.pawsandrequests.TestFixtures.shelter;
import static com.codecool.pawsandrequests.TestFixtures.shelterUser;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AdoptionFormMapper")
class AdoptionFormMapperTest {

    private AdoptionFormMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new AdoptionFormMapperImpl();
    }

    @Test
    @DisplayName("flattens the applicant and animal onto the response")
    void flattensResponse() {
        Shelter shelter = shelter();
        Animal animal = animal(shelter);
        User applicant = shelterUser(shelter);
        applicant.setFirstName("Grace");
        applicant.setLastName("Hopper");

        AdoptionForm form = new AdoptionForm();
        form.setContent("We have a big garden");
        form.setUser(applicant);
        form.setPost(post(shelterUser(shelter), animal));

        AdoptionFormResponse result = mapper.toAdoptionFormResponse(form);

        assertThat(result.content()).isEqualTo("We have a big garden");
        assertThat(result.firstName()).isEqualTo("Grace");
        assertThat(result.lastName()).isEqualTo("Hopper");
        assertThat(result.email()).isEqualTo(EMAIL);
        assertThat(result.animalId()).isEqualTo(animal.getId());
    }

    @Test
    @DisplayName("returns null for a null form")
    void nullInNullOut() {
        assertThat(mapper.toAdoptionFormResponse(null)).isNull();
    }

    @Test
    @DisplayName("maps a request onto a form linked to user and post")
    void mapsRequestToForm() {
        User applicant = shelterUser(shelter());
        Post target = post(shelterUser(shelter()), animal(shelter()));

        AdoptionForm result = mapper.toAdoptionForm(
                new AdoptionFormRequest("Please adopt me"), applicant, target
        );

        assertThat(result.getContent()).isEqualTo("Please adopt me");
        assertThat(result.getUser()).isSameAs(applicant);
        assertThat(result.getPost()).isSameAs(target);
    }

    @Test
    @DisplayName("leaves the generated id unset")
    void leavesIdUnset() {
        AdoptionForm result = mapper.toAdoptionForm(
                new AdoptionFormRequest("Hi"), shelterUser(shelter()),
                post(shelterUser(shelter()), animal(shelter()))
        );

        assertThat(result.getId()).isNull();
    }

    @Test
    @DisplayName("copies an empty content string verbatim")
    void copiesEmptyContent() {
        AdoptionForm result = mapper.toAdoptionForm(
                new AdoptionFormRequest(""), shelterUser(shelter()),
                post(shelterUser(shelter()), animal(shelter()))
        );

        assertThat(result.getContent()).isEmpty();
    }
}
