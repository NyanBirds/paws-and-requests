package com.codecool.pawsandrequests.config;

import com.codecool.pawsandrequests.model.Picture;
import com.codecool.pawsandrequests.repository.PictureRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Map;
import java.util.Optional;

/**
 * Writes the seed images from {@code src/main/resources/images} into the
 * {@code picture} table, replacing the placeholder bytes that
 * {@code db/seed/R__demo_data.sql} inserts.
 *
 * <p>This exists because image data does not belong in a SQL migration file.
 * The migration owns the rows and their links, this owns the bytes.
 *
 * <p>Disabled unless {@code app.seed-pictures.enabled} is true. The Azure
 * database contains real uploads that must not be replaced, so only the local
 * development database turns this on.
 */
@Component
@ConditionalOnProperty(name = "app.seed-pictures.enabled", havingValue = "true")
public final class SeedPictures implements ApplicationRunner {

    private static final String IMAGE_PATH = "images/";
    private static final String JPEG = "image/jpeg";

    private static final Map<Long, String> IMAGES = Map.ofEntries(
            Map.entry(1L, "maja.jpg"),
            Map.entry(2L, "maja2.jpg"),
            Map.entry(4L, "dyrebeskyttelsen.jpg"),
            Map.entry(5L, "dyreneshus.jpg"),
            Map.entry(6L, "dyrevernalliansen.jpg"),
            Map.entry(7L, "fod.jpg"),
            Map.entry(9L, "dyrebeskyttelsen.jpg"),
            Map.entry(12L, "maja2.jpg"),
            Map.entry(13L, "maja.jpg"),
            Map.entry(15L, "maja.jpg"),
            Map.entry(16L, "maja2.jpg"));

    private final PictureRepository pictureRepository;

    public SeedPictures(final PictureRepository repository) {
        this.pictureRepository = repository;
    }

    @Override
    public void run(final ApplicationArguments args) {
        IMAGES.forEach(this::writeImage);
    }

    private void writeImage(final Long pictureId, final String fileName) {
        final Optional<Picture> found = pictureRepository.findById(pictureId);
        if (found.isEmpty()) {
            return;
        }
        final Picture picture = found.get();
        picture.setData(readImage(fileName));
        picture.setContentType(JPEG);
        pictureRepository.save(picture);
    }

    private byte[] readImage(final String fileName) {
        final ClassPathResource resource =
                new ClassPathResource(IMAGE_PATH + fileName);
        try (InputStream stream = resource.getInputStream()) {
            return stream.readAllBytes();
        } catch (final IOException e) {
            throw new UncheckedIOException("Could not read " + fileName, e);
        }
    }
}
