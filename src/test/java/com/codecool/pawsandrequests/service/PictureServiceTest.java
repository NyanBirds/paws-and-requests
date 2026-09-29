package com.codecool.pawsandrequests.service;

import com.codecool.pawsandrequests.model.Picture;
import com.codecool.pawsandrequests.repository.PictureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Optional;

import static com.codecool.pawsandrequests.TestFixtures.picture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("PictureService")
class PictureServiceTest {

    @Mock
    private PictureRepository pictureRepository;

    private PictureService pictureService;

    @BeforeEach
    void setUp() {
        pictureService = new PictureService(pictureRepository);
    }

    @Test
    @DisplayName("returns the stored picture")
    void returnsStoredPicture() {
        Picture stored = picture(4L);
        when(pictureRepository.findById(4L)).thenReturn(Optional.of(stored));

        assertThat(pictureService.getPicture(4L)).isSameAs(stored);
    }

    @Test
    @DisplayName("throws 404 for an unknown picture")
    void throwsForUnknownPicture() {
        when(pictureRepository.findById(9L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pictureService.getPicture(9L))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(e -> assertThat(
                        ((ResponseStatusException) e).getStatusCode()
                ).isEqualTo(HttpStatus.NOT_FOUND));
    }

    @Test
    @DisplayName("stores the bytes and content type of an upload")
    void storesUpload() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getBytes()).thenReturn(new byte[]{1, 2, 3});
        when(file.getContentType()).thenReturn("image/png");
        when(pictureRepository.save(any(Picture.class))).thenAnswer(
                invocation -> invocation.getArgument(0)
        );

        Picture saved = pictureService.createPicture(file);

        assertThat(saved.getData()).containsExactly(1, 2, 3);
        assertThat(saved.getContentType()).isEqualTo("image/png");
    }

    @Test
    @DisplayName("rejects an unreadable upload with 400")
    void rejectsUnreadableUpload() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getBytes()).thenThrow(new IOException("broken stream"));

        Throwable thrown = catchThrowable(() -> pictureService
                .createPicture(file));

        assertThat(thrown).isInstanceOf(ResponseStatusException.class)
                .satisfies(e -> assertThat(
                        ((ResponseStatusException) e).getStatusCode()
                ).isEqualTo(HttpStatus.BAD_REQUEST));
        verify(pictureRepository, never()).save(any());
    }

    @Test
    @DisplayName("does not persist a picture when the read fails")
    void doesNotPersistOnReadFailure() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getBytes()).thenThrow(new IOException());

        org.assertj.core.api.Assertions
                .assertThatThrownBy(() -> pictureService.createPicture(file))
                .isInstanceOf(ResponseStatusException.class);

        verify(pictureRepository, never()).save(any(Picture.class));
    }

    @Test
    @DisplayName("deletes by id")
    void deletesById() {
        pictureService.deletePicture(11L);

        verify(pictureRepository).deleteById(11L);
    }

    @Test
    @DisplayName("translates a constraint violation into 409")
    void translatesConstraintViolation() {
        doThrow(new DataIntegrityViolationException("referenced"))
                .when(pictureRepository).deleteById(11L);

        assertThatThrownBy(() -> pictureService.deletePicture(11L))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(e -> assertThat(
                        ((ResponseStatusException) e).getStatusCode()
                ).isEqualTo(HttpStatus.CONFLICT));
    }

    @Test
    @DisplayName("captures the picture passed to save")
    void capturesSavedPicture() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getBytes()).thenReturn(new byte[]{9});
        when(file.getContentType()).thenReturn("image/gif");
        when(pictureRepository.save(any(Picture.class))).thenAnswer(
                invocation -> invocation.getArgument(0)
        );

        pictureService.createPicture(file);

        ArgumentCaptor<Picture> captor = ArgumentCaptor.forClass(
                Picture.class
        );
        verify(pictureRepository).save(captor.capture());
        assertThat(captor.getValue().getData()).containsExactly(9);
        assertThat(captor.getValue().getContentType())
                .isEqualTo("image/gif");
    }
}
