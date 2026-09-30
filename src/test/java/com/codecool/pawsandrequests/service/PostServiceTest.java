package com.codecool.pawsandrequests.service;

import com.codecool.pawsandrequests.dto.EditPostRequest;
import com.codecool.pawsandrequests.dto.PostRequest;
import com.codecool.pawsandrequests.dto.PostResponse;
import com.codecool.pawsandrequests.dto.PostSummaryResponse;
import com.codecool.pawsandrequests.exception.ResourceNotFoundException;
import com.codecool.pawsandrequests.mapper.PostMapper;
import com.codecool.pawsandrequests.model.Animal;
import com.codecool.pawsandrequests.model.Gender;
import com.codecool.pawsandrequests.model.Picture;
import com.codecool.pawsandrequests.model.Post;
import com.codecool.pawsandrequests.model.Role;
import com.codecool.pawsandrequests.model.Shelter;
import com.codecool.pawsandrequests.model.Species;
import com.codecool.pawsandrequests.model.User;
import com.codecool.pawsandrequests.repository.AnimalRepository;
import com.codecool.pawsandrequests.repository.PostRepository;
import com.codecool.pawsandrequests.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.codecool.pawsandrequests.TestFixtures.EMAIL;
import static com.codecool.pawsandrequests.TestFixtures.ORG_NR;
import static com.codecool.pawsandrequests.TestFixtures.OTHER_ORG_NR;
import static com.codecool.pawsandrequests.TestFixtures.animal;
import static com.codecool.pawsandrequests.TestFixtures.picture;
import static com.codecool.pawsandrequests.TestFixtures.post;
import static com.codecool.pawsandrequests.TestFixtures.shelter;
import static com.codecool.pawsandrequests.TestFixtures.shelterUser;
import static com.codecool.pawsandrequests.TestFixtures.user;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("PostService")
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private PostMapper postMapper;

    @Mock
    private PictureService pictureService;

    private PostService postService;

    @BeforeEach
    void setUp() {
        postService = new PostService(postRepository, userRepository,
                animalRepository, postMapper, pictureService);
    }

    private PostSummaryResponse summary(final Post post) {
        return new PostSummaryResponse(post.getId(), post.getTitle(), 3,
                Gender.FEMALE, Species.CAT, Optional.empty());
    }

    private PostResponse fullResponse(final Post post) {
        return new PostResponse(post.getId(), post.getTitle(),
                post.getDescription(), "Nyan Rescue", "Rainbow Road 1",
                List.of(), List.of(), 3, Gender.FEMALE, Species.CAT, "Maja", null);
    }

    @Nested
    @DisplayName("getAllPosts")
    class GetAllPosts {

        @Test
        @DisplayName("returns every post when no filter is given")
        void returnsAll() {
            Post first = post(shelterUser(shelter()), animal(shelter()));
            Post second = post(shelterUser(shelter()), animal(shelter()));
            when(postRepository.findAll()).thenReturn(List.of(first, second));
            when(postMapper.toPostSummaryResponse(first))
                    .thenReturn(summary(first));
            when(postMapper.toPostSummaryResponse(second))
                    .thenReturn(summary(second));

            assertThat(postService.getAllPosts(null, null)).hasSize(2);
        }

        @Test
        @DisplayName("filters by gender only")
        void filtersByGender() {
            List<Gender> genders = List.of(Gender.FEMALE);
            Post match = post(shelterUser(shelter()), animal(shelter()));
            when(postRepository.findByAnimalGenderIn(genders))
                    .thenReturn(List.of(match));
            when(postMapper.toPostSummaryResponse(match))
                    .thenReturn(summary(match));

            List<PostSummaryResponse> result = postService.getAllPosts(
                    genders, null
            );

            assertThat(result).hasSize(1);
            verify(postRepository, never()).findAll();
        }

        @Test
        @DisplayName("filters by species only")
        void filtersBySpecies() {
            List<Species> species = List.of(Species.CAT);
            Post match = post(shelterUser(shelter()), animal(shelter()));
            when(postRepository.findByAnimalSpeciesIn(species))
                    .thenReturn(List.of(match));
            when(postMapper.toPostSummaryResponse(match))
                    .thenReturn(summary(match));

            assertThat(postService.getAllPosts(null, species)).hasSize(1);
            verify(postRepository, never()).findAll();
        }

        @Test
        @DisplayName("combines both filters")
        void combinesFilters() {
            List<Gender> genders = List.of(Gender.FEMALE);
            List<Species> species = List.of(Species.CAT);
            Post match = post(shelterUser(shelter()), animal(shelter()));
            when(postRepository.findByAnimalGenderInAndAnimalSpeciesIn(
                    genders, species)).thenReturn(List.of(match));
            when(postMapper.toPostSummaryResponse(match))
                    .thenReturn(summary(match));

            assertThat(postService.getAllPosts(genders, species)).hasSize(1);
            verify(postRepository, never()).findAll();
            verify(postRepository, never()).findByAnimalGenderIn(anyList());
        }

        @Test
        @DisplayName("returns an empty list when nothing matches")
        void returnsEmpty() {
            when(postRepository.findAll()).thenReturn(List.of());

            assertThat(postService.getAllPosts(null, null)).isEmpty();
        }
    }

    @Nested
    @DisplayName("getOnePost")
    class GetOnePost {

        @Test
        @DisplayName("maps the requested post")
        void mapsPost() {
            Post target = post(shelterUser(shelter()), animal(shelter()));
            when(postRepository.findById(target.getId()))
                    .thenReturn(Optional.of(target));
            when(postMapper.toPostResponse(target))
                    .thenReturn(fullResponse(target));

            assertThat(postService.getOnePost(target.getId()).title())
                    .isEqualTo(target.getTitle());
        }

        @Test
        @DisplayName("throws 404 for an unknown post")
        void throwsForUnknown() {
            UUID missing = UUID.randomUUID();
            when(postRepository.findById(missing))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> postService.getOnePost(missing))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("getAllPostsByShelter")
    class GetAllPostsByShelter {

        @Test
        @DisplayName("returns the caller's shelter posts")
        void returnsShelterPosts() {
            Shelter mine = shelter();
            Post target = post(shelterUser(mine), animal(mine));
            when(userRepository.findByEmail(EMAIL)).thenReturn(
                    Optional.of(shelterUser(mine))
            );
            when(postRepository.findByUserShelterOrgNr(ORG_NR))
                    .thenReturn(List.of(target));
            when(postMapper.toPostSummaryResponse(target))
                    .thenReturn(summary(target));

            assertThat(postService.getAllPostsByShelter(EMAIL)).hasSize(1);
        }

        @Test
        @DisplayName("forbids a user who is not shelter staff")
        void forbidsNonStaff() {
            when(userRepository.findByEmail(EMAIL))
                    .thenReturn(Optional.of(user(Role.USER, null)));

            assertThatThrownBy(() -> postService.getAllPostsByShelter(EMAIL))
                    .isInstanceOf(ResponseStatusException.class)
                    .satisfies(e -> assertThat(
                            ((ResponseStatusException) e).getStatusCode()
                    ).isEqualTo(HttpStatus.FORBIDDEN));
        }

        @Test
        @DisplayName("forbids an admin, who has no shelter of their own")
        void forbidsAdmin() {
            when(userRepository.findByEmail(EMAIL)).thenReturn(
                    Optional.of(com.codecool.pawsandrequests.TestFixtures
                            .admin())
            );

            assertThatThrownBy(() -> postService.getAllPostsByShelter(EMAIL))
                    .isInstanceOf(ResponseStatusException.class);
        }
    }

    @Nested
    @DisplayName("getShelterPosts")
    class GetShelterPosts {

        @Test
        @DisplayName("returns posts for the given org number")
        void returnsPostsForOrgNumber() {
            Post target = post(shelterUser(shelter()), animal(shelter()));
            when(postRepository.findByUserShelterOrgNr(ORG_NR))
                    .thenReturn(List.of(target));
            when(postMapper.toPostSummaryResponse(target))
                    .thenReturn(summary(target));

            assertThat(postService.getShelterPosts(ORG_NR)).hasSize(1);
        }

        @Test
        @DisplayName("needs no authenticated user")
        void needsNoUser() {
            when(postRepository.findByUserShelterOrgNr(ORG_NR))
                    .thenReturn(List.of());

            postService.getShelterPosts(ORG_NR);

            verifyNoInteractions(userRepository);
        }
    }

    @Nested
    @DisplayName("deletePost")
    class DeletePost {

        @Test
        @DisplayName("an admin can delete any post")
        void adminDeletesAny() {
            Post target = post(shelterUser(shelter(OTHER_ORG_NR)),
                    animal(shelter(OTHER_ORG_NR)));
            when(postRepository.findById(target.getId()))
                    .thenReturn(Optional.of(target));
            when(userRepository.findByEmail(EMAIL)).thenReturn(
                    Optional.of(com.codecool.pawsandrequests.TestFixtures
                            .admin())
            );

            postService.deletePost(target.getId(), EMAIL);

            verify(postRepository).delete(target);
        }

        @Test
        @DisplayName("shelter staff can delete their own post")
        void staffDeletesOwn() {
            Shelter mine = shelter();
            Post target = post(shelterUser(mine), animal(mine));
            when(postRepository.findById(target.getId()))
                    .thenReturn(Optional.of(target));
            when(userRepository.findByEmail(EMAIL))
                    .thenReturn(Optional.of(shelterUser(mine)));

            postService.deletePost(target.getId(), EMAIL);

            verify(postRepository).delete(target);
        }

        @Test
        @DisplayName("shelter staff cannot delete another shelter's post")
        void staffCannotDeleteForeign() {
            Post foreign = post(shelterUser(shelter(OTHER_ORG_NR)),
                    animal(shelter(OTHER_ORG_NR)));
            when(postRepository.findById(foreign.getId()))
                    .thenReturn(Optional.of(foreign));
            when(userRepository.findByEmail(EMAIL))
                    .thenReturn(Optional.of(shelterUser(shelter())));

            assertThatThrownBy(() -> postService.deletePost(foreign.getId(),
                    EMAIL))
                    .isInstanceOf(ResponseStatusException.class)
                    .satisfies(e -> assertThat(
                            ((ResponseStatusException) e).getStatusCode()
                    ).isEqualTo(HttpStatus.FORBIDDEN));

            verify(postRepository, never()).delete(any());
        }

        @Test
        @DisplayName("a regular user cannot delete")
        void plainUserCannotDelete() {
            Post target = post(shelterUser(shelter()), animal(shelter()));
            when(postRepository.findById(target.getId()))
                    .thenReturn(Optional.of(target));
            when(userRepository.findByEmail(EMAIL))
                    .thenReturn(Optional.of(user(Role.USER, null)));

            assertThatThrownBy(() -> postService.deletePost(target.getId(),
                    EMAIL))
                    .isInstanceOf(ResponseStatusException.class)
                    .satisfies(e -> assertThat(
                            ((ResponseStatusException) e).getStatusCode()
                    ).isEqualTo(HttpStatus.FORBIDDEN));
        }

        @Test
        @DisplayName("throws 404 for an unknown post")
        void throwsForUnknownPost() {
            UUID missing = UUID.randomUUID();
            when(postRepository.findById(missing))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> postService.deletePost(missing, EMAIL))
                    .isInstanceOf(ResponseStatusException.class)
                    .satisfies(e -> assertThat(
                            ((ResponseStatusException) e).getStatusCode()
                ).isEqualTo(HttpStatus.NOT_FOUND));
        }

        @Test
        @DisplayName("throws 404 for an unknown user")
        void throwsForUnknownUser() {
            Post target = post(shelterUser(shelter()), animal(shelter()));
            when(postRepository.findById(target.getId()))
                    .thenReturn(Optional.of(target));
            when(userRepository.findByEmail(EMAIL))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> postService.deletePost(target.getId(),
                    EMAIL))
                    .isInstanceOf(ResponseStatusException.class)
                    .satisfies(e -> assertThat(
                            ((ResponseStatusException) e).getStatusCode()
                    ).isEqualTo(HttpStatus.NOT_FOUND));

            verify(postRepository, never()).delete(any());
        }
    }

    @Nested
    @DisplayName("createPost")
    class CreatePost {

        @Test
        @DisplayName("creates a post for an animal in the shelter")
        void createsPost() {
            Shelter mine = shelter();
            Animal target = animal(mine);
            User staff = shelterUser(mine);
            Post created = post(staff, target);
            PostRequest request = new PostRequest("New title", "New desc",
                    target.getId());

            when(userRepository.findByEmail(EMAIL))
                    .thenReturn(Optional.of(staff));
            when(animalRepository.findById(target.getId()))
                    .thenReturn(Optional.of(target));
            when(postMapper.toPost(request, staff, target))
                    .thenReturn(created);
            when(postMapper.toPostResponse(created))
                    .thenReturn(fullResponse(created));

            PostResponse response = postService.createPost(request, null,
                    ORG_NR, EMAIL);

            assertThat(response.title()).isEqualTo(created.getTitle());
            verify(postRepository).save(created);
        }

        @Test
        @DisplayName("attaches the uploaded pictures")
        void attachesPictures() throws IOException {
            Shelter mine = shelter();
            Animal target = animal(mine);
            User staff = shelterUser(mine);
            Post created = post(staff, target);
            PostRequest request = new PostRequest("t", "d", target.getId());

            MultipartFile file = mock(MultipartFile.class);
            when(file.getBytes()).thenReturn(new byte[]{7, 7});
            when(file.getContentType()).thenReturn("image/jpeg");

            when(userRepository.findByEmail(EMAIL))
                    .thenReturn(Optional.of(staff));
            when(animalRepository.findById(target.getId()))
                    .thenReturn(Optional.of(target));
            when(postMapper.toPost(request, staff, target))
                    .thenReturn(created);
            when(postMapper.toPostResponse(created))
                    .thenReturn(fullResponse(created));

            postService.createPost(request, List.of(file), ORG_NR, EMAIL);

            ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(
                    Post.class
            );
            verify(postRepository).save(captor.capture());
            assertThat(captor.getValue().getPictures()).hasSize(1);
            assertThat(captor.getValue().getPictures().getFirst()
                    .getContentType()).isEqualTo("image/jpeg");
        }

        @Test
        @DisplayName("leaves pictures empty when none are uploaded")
        void noPictures() {
            Shelter mine = shelter();
            Animal target = animal(mine);
            User staff = shelterUser(mine);
            Post created = post(staff, target);
            PostRequest request = new PostRequest("t", "d", target.getId());

            when(userRepository.findByEmail(EMAIL))
                    .thenReturn(Optional.of(staff));
            when(animalRepository.findById(target.getId()))
                    .thenReturn(Optional.of(target));
            when(postMapper.toPost(request, staff, target))
                    .thenReturn(created);
            when(postMapper.toPostResponse(created))
                    .thenReturn(fullResponse(created));

            postService.createPost(request, null, ORG_NR, EMAIL);

            assertThat(created.getPictures()).isEmpty();
        }

        @Test
        @DisplayName("rejects an unreadable upload with 400")
        void rejectsUnreadableUpload() throws IOException {
            Shelter mine = shelter();
            Animal target = animal(mine);
            User staff = shelterUser(mine);
            Post created = post(staff, target);
            PostRequest request = new PostRequest("t", "d", target.getId());

            MultipartFile file = mock(MultipartFile.class);
            when(file.getBytes()).thenThrow(new IOException());

            when(userRepository.findByEmail(EMAIL))
                    .thenReturn(Optional.of(staff));
            when(animalRepository.findById(target.getId()))
                    .thenReturn(Optional.of(target));
            when(postMapper.toPost(request, staff, target))
                    .thenReturn(created);

            assertThatThrownBy(() -> postService.createPost(request,
                    List.of(file), ORG_NR, EMAIL))
                    .isInstanceOf(ResponseStatusException.class)
                    .satisfies(e -> assertThat(
                            ((ResponseStatusException) e).getStatusCode()
                    ).isEqualTo(HttpStatus.BAD_REQUEST));

            verify(postRepository, never()).save(any());
        }

        @Test
        @DisplayName("rejects an animal from another shelter")
        void rejectsForeignAnimal() {
            Animal foreign = animal(shelter(OTHER_ORG_NR));
            PostRequest request = new PostRequest("t", "d", foreign.getId());

            when(userRepository.findByEmail(EMAIL)).thenReturn(
                    Optional.of(shelterUser(shelter()))
            );
            when(animalRepository.findById(foreign.getId()))
                    .thenReturn(Optional.of(foreign));

            assertThatThrownBy(() -> postService.createPost(request, null,
                    ORG_NR, EMAIL))
                    .isInstanceOf(ResponseStatusException.class)
                    .satisfies(e -> assertThat(
                            ((ResponseStatusException) e).getStatusCode()
                    ).isEqualTo(HttpStatus.FORBIDDEN));

            verify(postRepository, never()).save(any());
        }

        @Test
        @DisplayName("throws 404 for an unknown animal")
        void throwsForUnknownAnimal() {
            UUID missing = UUID.randomUUID();
            PostRequest request = new PostRequest("t", "d", missing);

            when(userRepository.findByEmail(EMAIL))
                    .thenReturn(Optional.of(shelterUser(shelter())));
            when(animalRepository.findById(missing))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> postService.createPost(request, null,
                    ORG_NR, EMAIL))
                    .isInstanceOf(ResponseStatusException.class)
                    .satisfies(e -> assertThat(
                            ((ResponseStatusException) e).getStatusCode()
                    ).isEqualTo(HttpStatus.NOT_FOUND));
        }

        @Test
        @DisplayName("throws 404 for an unknown user")
        void throwsForUnknownUser() {
            PostRequest request = new PostRequest("t", "d", UUID.randomUUID());
            when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> postService.createPost(request, null,
                    ORG_NR, EMAIL))
                    .isInstanceOf(ResponseStatusException.class)
                    .satisfies(e -> assertThat(
                            ((ResponseStatusException) e).getStatusCode()
                    ).isEqualTo(HttpStatus.NOT_FOUND));

            verifyNoInteractions(animalRepository);
        }
    }

    @Nested
    @DisplayName("editPost")
    class EditPost {

        @Test
        @DisplayName("updates title and description")
        void updatesFields() {
            Shelter mine = shelter();
            Post target = post(shelterUser(mine), animal(mine));

            when(postRepository.findById(target.getId()))
                    .thenReturn(Optional.of(target));
            when(userRepository.findByEmail(EMAIL))
                    .thenReturn(Optional.of(shelterUser(mine)));
            when(postMapper.toPostResponse(target))
                    .thenReturn(fullResponse(target));

            postService.editPost(new EditPostRequest("New", "Newer"), null,
                    target.getId(), EMAIL);

            assertThat(target.getTitle()).isEqualTo("New");
            assertThat(target.getDescription()).isEqualTo("Newer");
            verify(postRepository).save(target);
        }

        @Test
        @DisplayName("leaves fields untouched when the request is null")
        void nullRequestChangesNothing() {
            Shelter mine = shelter();
            Post target = post(shelterUser(mine), animal(mine));
            String title = target.getTitle();
            String description = target.getDescription();

            when(postRepository.findById(target.getId()))
                    .thenReturn(Optional.of(target));
            when(userRepository.findByEmail(EMAIL))
                    .thenReturn(Optional.of(shelterUser(mine)));
            when(postMapper.toPostResponse(target))
                    .thenReturn(fullResponse(target));

            postService.editPost(null, null, target.getId(), EMAIL);

            assertThat(target.getTitle()).isEqualTo(title);
            assertThat(target.getDescription()).isEqualTo(description);
        }

        @Test
        @DisplayName("only overwrites the fields that were provided")
        void partialUpdate() {
            Shelter mine = shelter();
            Post target = post(shelterUser(mine), animal(mine));
            String description = target.getDescription();

            when(postRepository.findById(target.getId()))
                    .thenReturn(Optional.of(target));
            when(userRepository.findByEmail(EMAIL))
                    .thenReturn(Optional.of(shelterUser(mine)));
            when(postMapper.toPostResponse(target))
                    .thenReturn(fullResponse(target));

            postService.editPost(new EditPostRequest("Only title", null),
                    null, target.getId(), EMAIL);

            assertThat(target.getTitle()).isEqualTo("Only title");
            assertThat(target.getDescription()).isEqualTo(description);
        }

        @Test
        @DisplayName("appends newly uploaded pictures")
        void appendsPictures() {
            Shelter mine = shelter();
            Post target = post(shelterUser(mine), animal(mine));
            Picture stored = picture(12L);

            when(postRepository.findById(target.getId()))
                    .thenReturn(Optional.of(target));
            when(userRepository.findByEmail(EMAIL))
                    .thenReturn(Optional.of(shelterUser(mine)));
            when(pictureService.createPicture(any(MultipartFile.class)))
                    .thenReturn(stored);
            when(postMapper.toPostResponse(target))
                    .thenReturn(fullResponse(target));

            postService.editPost(null, List.of(mock(MultipartFile.class)),
                    target.getId(), EMAIL);

            assertThat(target.getPictures()).containsExactly(stored);
            verify(postRepository).save(target);
        }

        @Test
        @DisplayName("an admin can edit any post")
        void adminEditsAny() {
            Post foreign = post(shelterUser(shelter(OTHER_ORG_NR)),
                    animal(shelter(OTHER_ORG_NR)));

            when(postRepository.findById(foreign.getId()))
                    .thenReturn(Optional.of(foreign));
            when(userRepository.findByEmail(EMAIL)).thenReturn(
                    Optional.of(com.codecool.pawsandrequests.TestFixtures
                            .admin())
            );
            when(postMapper.toPostResponse(foreign))
                    .thenReturn(fullResponse(foreign));

            postService.editPost(new EditPostRequest("Edited", null), null,
                    foreign.getId(), EMAIL);

            assertThat(foreign.getTitle()).isEqualTo("Edited");
        }

        @Test
        @DisplayName("shelter staff cannot edit another shelter's post")
        void staffCannotEditForeign() {
            Post foreign = post(shelterUser(shelter(OTHER_ORG_NR)),
                    animal(shelter(OTHER_ORG_NR)));

            when(postRepository.findById(foreign.getId()))
                    .thenReturn(Optional.of(foreign));
            when(userRepository.findByEmail(EMAIL))
                    .thenReturn(Optional.of(shelterUser(shelter())));

            assertThatThrownBy(() -> postService.editPost(
                    new EditPostRequest("x", null), null, foreign.getId(),
                    EMAIL))
                    .isInstanceOf(ResponseStatusException.class)
                    .satisfies(e -> assertThat(
                            ((ResponseStatusException) e).getStatusCode()
                    ).isEqualTo(HttpStatus.FORBIDDEN));
        }

        @Test
        @DisplayName("a regular user cannot edit")
        void plainUserCannotEdit() {
            Post target = post(shelterUser(shelter()), animal(shelter()));
            when(postRepository.findById(target.getId()))
                    .thenReturn(Optional.of(target));
            when(userRepository.findByEmail(EMAIL))
                    .thenReturn(Optional.of(user(Role.USER, null)));

            assertThatThrownBy(() -> postService.editPost(
                    new EditPostRequest("x", null), null, target.getId(),
                    EMAIL))
                    .isInstanceOf(ResponseStatusException.class)
                    .satisfies(e -> assertThat(
                            ((ResponseStatusException) e).getStatusCode()
                    ).isEqualTo(HttpStatus.FORBIDDEN));
        }

        @Test
        @DisplayName("throws 404 for an unknown post")
        void throwsForUnknownPost() {
            UUID missing = UUID.randomUUID();
            when(postRepository.findById(missing))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> postService.editPost(null, null,
                    missing, EMAIL))
                    .isInstanceOf(ResponseStatusException.class)
                    .satisfies(e -> assertThat(
                            ((ResponseStatusException) e).getStatusCode()
                    ).isEqualTo(HttpStatus.NOT_FOUND));
        }
    }

    @Nested
    @DisplayName("deletePostPicture")
    class DeletePostPicture {

        @Test
        @DisplayName("removes the picture and deletes the row")
        void removesPicture() {
            Shelter mine = shelter();
            Post target = post(shelterUser(mine), animal(mine));
            Picture owned = picture(5L);
            target.getPictures().add(owned);

            when(postRepository.findById(target.getId()))
                    .thenReturn(Optional.of(target));
            when(userRepository.findByEmail(EMAIL))
                    .thenReturn(Optional.of(shelterUser(mine)));

            postService.deletePostPicture(target.getId(), EMAIL, 5L);

            assertThat(target.getPictures()).isEmpty();
            verify(pictureService).deletePicture(5L);
            verify(postRepository).save(target);
        }

        @Test
        @DisplayName("keeps the other pictures")
        void keepsOtherPictures() {
            Shelter mine = shelter();
            Post target = post(shelterUser(mine), animal(mine));
            target.getPictures().add(picture(5L));
            Picture keep = picture(6L);
            target.getPictures().add(keep);

            when(postRepository.findById(target.getId()))
                    .thenReturn(Optional.of(target));
            when(userRepository.findByEmail(EMAIL))
                    .thenReturn(Optional.of(shelterUser(mine)));

            postService.deletePostPicture(target.getId(), EMAIL, 5L);

            assertThat(target.getPictures()).containsExactly(keep);
        }

        @Test
        @DisplayName("throws 404 when the picture is not on the post")
        void throwsForUnknownPicture() {
            Shelter mine = shelter();
            Post target = post(shelterUser(mine), animal(mine));
            target.getPictures().add(picture(5L));

            when(postRepository.findById(target.getId()))
                    .thenReturn(Optional.of(target));
            when(userRepository.findByEmail(EMAIL))
                    .thenReturn(Optional.of(shelterUser(mine)));

            assertThatThrownBy(() -> postService.deletePostPicture(
                    target.getId(), EMAIL, 99L))
                    .isInstanceOf(ResponseStatusException.class)
                    .satisfies(e -> assertThat(
                            ((ResponseStatusException) e).getStatusCode()
                    ).isEqualTo(HttpStatus.NOT_FOUND));

            verify(pictureService, never()).deletePicture(any());
        }

        @Test
        @DisplayName("an admin can remove any picture")
        void adminRemovesAny() {
            Post foreign = post(shelterUser(shelter(OTHER_ORG_NR)),
                    animal(shelter(OTHER_ORG_NR)));
            foreign.getPictures().add(picture(5L));

            when(postRepository.findById(foreign.getId()))
                    .thenReturn(Optional.of(foreign));
            when(userRepository.findByEmail(EMAIL)).thenReturn(
                    Optional.of(com.codecool.pawsandrequests.TestFixtures
                            .admin())
            );

            postService.deletePostPicture(foreign.getId(), EMAIL, 5L);

            verify(pictureService).deletePicture(5L);
        }

        @Test
        @DisplayName("shelter staff cannot remove another shelter's picture")
        void staffCannotRemoveForeign() {
            Post foreign = post(shelterUser(shelter(OTHER_ORG_NR)),
                    animal(shelter(OTHER_ORG_NR)));
            foreign.getPictures().add(picture(5L));

            when(postRepository.findById(foreign.getId()))
                    .thenReturn(Optional.of(foreign));
            when(userRepository.findByEmail(EMAIL))
                    .thenReturn(Optional.of(shelterUser(shelter())));

            assertThatThrownBy(() -> postService.deletePostPicture(
                    foreign.getId(), EMAIL, 5L))
                    .isInstanceOf(ResponseStatusException.class)
                    .satisfies(e -> assertThat(
                            ((ResponseStatusException) e).getStatusCode()
                    ).isEqualTo(HttpStatus.FORBIDDEN));
        }

        @Test
        @DisplayName("a regular user cannot remove pictures")
        void plainUserCannotRemove() {
            Post target = post(shelterUser(shelter()), animal(shelter()));
            target.getPictures().add(picture(5L));

            when(postRepository.findById(target.getId()))
                    .thenReturn(Optional.of(target));
            when(userRepository.findByEmail(EMAIL))
                    .thenReturn(Optional.of(user(Role.USER, null)));

            assertThatThrownBy(() -> postService.deletePostPicture(
                    target.getId(), EMAIL, 5L))
                    .isInstanceOf(ResponseStatusException.class)
                    .satisfies(e -> assertThat(
                            ((ResponseStatusException) e).getStatusCode()
                    ).isEqualTo(HttpStatus.FORBIDDEN));
        }

        @Test
        @DisplayName("throws 404 for an unknown post")
        void throwsForUnknownPost() {
            UUID missing = UUID.randomUUID();
            when(postRepository.findById(missing))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> postService.deletePostPicture(missing,
                    EMAIL, 5L))
                    .isInstanceOf(ResponseStatusException.class)
                    .satisfies(e -> assertThat(
                            ((ResponseStatusException) e).getStatusCode()
                    ).isEqualTo(HttpStatus.NOT_FOUND));
        }
    }
}
