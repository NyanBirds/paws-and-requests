package com.codecool.pawsandrequests.service;

import com.codecool.pawsandrequests.dto.AdoptionFormRequest;
import com.codecool.pawsandrequests.dto.AdoptionFormResponse;
import com.codecool.pawsandrequests.mapper.AdoptionFormMapper;
import com.codecool.pawsandrequests.model.AdoptionForm;
import com.codecool.pawsandrequests.model.Post;
import com.codecool.pawsandrequests.model.Role;
import com.codecool.pawsandrequests.model.Shelter;
import com.codecool.pawsandrequests.model.User;
import com.codecool.pawsandrequests.repository.AdoptionFormRepository;
import com.codecool.pawsandrequests.repository.PostRepository;
import com.codecool.pawsandrequests.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.codecool.pawsandrequests.TestFixtures.EMAIL;
import static com.codecool.pawsandrequests.TestFixtures.ORG_NR;
import static com.codecool.pawsandrequests.TestFixtures.OTHER_ORG_NR;
import static com.codecool.pawsandrequests.TestFixtures.admin;
import static com.codecool.pawsandrequests.TestFixtures.animal;
import static com.codecool.pawsandrequests.TestFixtures.post;
import static com.codecool.pawsandrequests.TestFixtures.shelter;
import static com.codecool.pawsandrequests.TestFixtures.shelterUser;
import static com.codecool.pawsandrequests.TestFixtures.user;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("AdoptionFormService")
class AdoptionFormServiceTest {

    @Mock
    private AdoptionFormRepository adoptionFormRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AdoptionFormMapper adoptionFormMapper;

    private AdoptionFormService adoptionFormService;

    @BeforeEach
    void setUp() {
        adoptionFormService = new AdoptionFormService(
                adoptionFormRepository, postRepository, userRepository,
                adoptionFormMapper
        );
    }

    private AdoptionForm form(final String content) {
        AdoptionForm form = new AdoptionForm();
        form.setContent(content);
        return form;
    }

    @Nested
    @DisplayName("getForms")
    class GetForms {

        @Test
        @DisplayName("returns forms on a post from the caller's shelter")
        void returnsOwnShelterForms() {
            Shelter mine = shelter();
            User staff = shelterUser(mine);
            Post target = post(staff, animal(mine));
            AdoptionForm form = form("We have a big garden");
            target.getAdoptionForms().add(form);

            when(userRepository.findByEmail(EMAIL))
                    .thenReturn(Optional.of(staff));
            when(postRepository.findById(target.getId()))
                    .thenReturn(Optional.of(target));
            when(adoptionFormMapper.toAdoptionFormResponse(form))
                    .thenReturn(new AdoptionFormResponse(form.getContent(),
                            "Ada", "Lovelace", EMAIL,
                            target.getAnimal().getId()));

            List<AdoptionFormResponse> result = adoptionFormService.getForms(
                    EMAIL, target.getId()
            );

            assertThat(result).hasSize(1);
            assertThat(result.getFirst().content())
                    .isEqualTo("We have a big garden");
        }

        @Test
        @DisplayName("returns an empty list when the post has no forms")
        void returnsEmptyList() {
            Shelter mine = shelter();
            User staff = shelterUser(mine);
            Post target = post(staff, animal(mine));

            when(userRepository.findByEmail(EMAIL))
                    .thenReturn(Optional.of(staff));
            when(postRepository.findById(target.getId()))
                    .thenReturn(Optional.of(target));

            assertThat(adoptionFormService.getForms(EMAIL, target.getId()))
                    .isEmpty();
        }

        @Test
        @DisplayName("forbids reading a post from another shelter")
        void forbidsOtherShelter() {
            Shelter mine = shelter();
            User staff = shelterUser(mine);
            Post foreign = post(shelterUser(shelter(OTHER_ORG_NR)),
                    animal(shelter(OTHER_ORG_NR)));

            when(userRepository.findByEmail(EMAIL))
                    .thenReturn(Optional.of(staff));
            when(postRepository.findById(foreign.getId()))
                    .thenReturn(Optional.of(foreign));

            assertThatThrownBy(() -> adoptionFormService.getForms(
                    EMAIL, foreign.getId()))
                    .isInstanceOf(ResponseStatusException.class)
                    .satisfies(e -> assertThat(
                            ((ResponseStatusException) e).getStatusCode()
                    ).isEqualTo(HttpStatus.FORBIDDEN));
        }

        @Test
        @DisplayName("forbids a user with no shelter")
        void forbidsUserWithoutShelter() {
            User plain = shelterUser(shelter());
            plain.setShelter(null);
            Post target = post(shelterUser(shelter()),
                    animal(shelter()));

            when(userRepository.findByEmail(EMAIL))
                    .thenReturn(Optional.of(plain));
            when(postRepository.findById(target.getId()))
                    .thenReturn(Optional.of(target));

            assertThatThrownBy(() -> adoptionFormService.getForms(
                    EMAIL, target.getId()))
                    .isInstanceOf(ResponseStatusException.class)
                    .satisfies(e -> assertThat(
                            ((ResponseStatusException) e).getStatusCode()
                    ).isEqualTo(HttpStatus.FORBIDDEN));
        }
    }

    @Nested
    @DisplayName("createForm")
    class CreateForm {

        @Test
        @DisplayName("saves the mapped form against the user and post")
        void savesMappedForm() {
            Shelter mine = shelter();
            User staff = shelterUser(mine);
            Post target = post(staff, animal(mine));
            AdoptionForm mapped = form("Please adopt me");

            when(userRepository.findByEmail(EMAIL))
                    .thenReturn(Optional.of(staff));
            when(postRepository.findById(target.getId()))
                    .thenReturn(Optional.of(target));
            when(adoptionFormMapper.toAdoptionForm(
                    any(AdoptionFormRequest.class),
                    eq(staff),
                    eq(target)
            )).thenReturn(mapped);

            adoptionFormService.createForm(EMAIL, target.getId(),
                    new AdoptionFormRequest("Please adopt me"));

            verify(adoptionFormRepository).save(mapped);
        }

        @Test
        @DisplayName("does not require shelter ownership to apply")
        void allowsAnyUserToApply() {
            User adopter = user(
                    Role.USER, null);
            adopter.setEmail("guest@example.com");
            Post target = post(shelterUser(shelter()),
                    animal(shelter()));
            AdoptionForm mapped = form("Hi");

            when(userRepository.findByEmail("guest@example.com"))
                    .thenReturn(Optional.of(adopter));
            when(postRepository.findById(target.getId()))
                    .thenReturn(Optional.of(target));
            when(adoptionFormMapper.toAdoptionForm(
                    any(AdoptionFormRequest.class),
                    eq(adopter),
                    eq(target)
            )).thenReturn(mapped);

            adoptionFormService.createForm("guest@example.com",
                    target.getId(), new AdoptionFormRequest("Hi"));

            verify(adoptionFormRepository).save(mapped);
        }
    }

    @Nested
    @DisplayName("getAllForms")
    class GetAllForms {

        @Test
        @DisplayName("an admin sees every form")
        void adminSeesAll() {
            User admin = admin();
            AdoptionForm first = form("one");
            AdoptionForm second = form("two");

            when(userRepository.findByEmail(EMAIL))
                    .thenReturn(Optional.of(admin));
            when(adoptionFormRepository.findAll())
                    .thenReturn(List.of(first, second));
            when(adoptionFormMapper.toAdoptionFormResponse(first))
                    .thenReturn(new AdoptionFormResponse("one", "A", "B",
                            EMAIL, UUID.randomUUID()));
            when(adoptionFormMapper.toAdoptionFormResponse(second))
                    .thenReturn(new AdoptionFormResponse("two", "C", "D",
                            EMAIL, UUID.randomUUID()));

            assertThat(adoptionFormService.getAllForms(EMAIL)).hasSize(2);
            verify(adoptionFormRepository, never())
                    .findByPostUserShelterOrgNr(any());
        }

        @Test
        @DisplayName("shelter staff only see their own shelter's forms")
        void shelterUserScopedToShelter() {
            Shelter mine = shelter();
            AdoptionForm form = form("mine");

            when(userRepository.findByEmail(EMAIL)).thenReturn(
                    Optional.of(shelterUser(mine))
            );
            when(adoptionFormRepository.findByPostUserShelterOrgNr(ORG_NR))
                    .thenReturn(List.of(form));
            when(adoptionFormMapper.toAdoptionFormResponse(form))
                    .thenReturn(new AdoptionFormResponse("mine", "Ada",
                            "Lovelace", EMAIL, UUID.randomUUID()));

            assertThat(adoptionFormService.getAllForms(EMAIL)).hasSize(1);

            verify(adoptionFormRepository)
                    .findByPostUserShelterOrgNr(ORG_NR);
            verify(adoptionFormRepository, never()).findAll();
        }

        @Test
        @DisplayName("a regular user is refused")
        void plainUserRefused() {
            when(userRepository.findByEmail(EMAIL)).thenReturn(
                    Optional.of(user(
                            Role.USER, null))
            );

            assertThatThrownBy(() -> adoptionFormService.getAllForms(EMAIL))
                    .isInstanceOf(ResponseStatusException.class)
                    .satisfies(e -> assertThat(
                            ((ResponseStatusException) e).getStatusCode()
                    ).isEqualTo(HttpStatus.FORBIDDEN));
        }
    }
}
