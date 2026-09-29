package com.codecool.pawsandrequests;

import com.codecool.pawsandrequests.model.Animal;
import com.codecool.pawsandrequests.model.CustomUserDetails;
import com.codecool.pawsandrequests.model.Gender;
import com.codecool.pawsandrequests.model.Picture;
import com.codecool.pawsandrequests.model.Post;
import com.codecool.pawsandrequests.model.Role;
import com.codecool.pawsandrequests.model.Shelter;
import com.codecool.pawsandrequests.model.Species;
import com.codecool.pawsandrequests.model.User;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

/**
 * Builders for the domain objects the service and mapper tests need.
 *
 * <p>These produce detached, in-memory entities. Nothing here touches a
 * repository or the Spring context.
 */
public final class TestFixtures {

    public static final String ORG_NR = "556677-8899";
    public static final String OTHER_ORG_NR = "111222-3344";
    public static final String EMAIL = "ada@example.com";
    public static final String RAW_PASSWORD = "correct-horse-battery";

    /**
     * A throwaway HS256 key. Deliberately unrelated to any real
     * {@code JWT_SECRET} so the test suite never depends on deployment
     * configuration.
     */
    public static final String JWT_SECRET = Base64.getEncoder().encodeToString(
            "paws-and-requests-unit-test-signing-key!".getBytes(
                    StandardCharsets.UTF_8)
    );

    public static final String OTHER_JWT_SECRET = Base64.getEncoder()
            .encodeToString(
                    "a-completely-different-unit-test-key!!".getBytes(
                            StandardCharsets.UTF_8)
            );

    private TestFixtures() {
    }

    public static Shelter shelter() {
        return shelter(ORG_NR);
    }

    public static Shelter shelter(final String orgNr) {
        Shelter shelter = new Shelter();
        shelter.setOrgNr(orgNr);
        shelter.setShelterName("Nyan Rescue " + orgNr);
        shelter.setAddress("Rainbow Road 1");
        shelter.setDescription("We keep cats and dogs.");
        return shelter;
    }

    public static Animal animal(final Shelter shelter) {
        return animal(shelter, "Maja", 3, Gender.FEMALE, Species.CAT);
    }

    public static Animal animal(
            final Shelter shelter,
            final String name,
            final int age,
            final Gender gender,
            final Species species
    ) {
        Animal animal = new Animal();
        animal.setId(UUID.randomUUID());
        animal.setName(name);
        animal.setAge(age);
        animal.setGender(gender);
        animal.setSpecies(species);
        animal.setShelter(shelter);
        return animal;
    }

    public static User user(final Role role, final Shelter shelter) {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setFirstName("Ada");
        user.setLastName("Lovelace");
        user.setEmail(EMAIL);
        user.setPhoneNumber("070-1234567");
        user.setPassword("{bcrypt}" + RAW_PASSWORD);
        user.setRole(role);
        user.setShelter(shelter);
        return user;
    }

    public static User shelterUser(final Shelter shelter) {
        return user(Role.SHELTERUSER, shelter);
    }

    public static User admin() {
        return user(Role.ADMIN, null);
    }

    public static Post post(final User user, final Animal animal) {
        Post post = new Post();
        post.setId(UUID.randomUUID());
        post.setTitle("Maja needs a home");
        post.setDescription("She is very good with children.");
        post.setUser(user);
        post.setAnimal(animal);
        return post;
    }

    public static Picture picture(final long id) {
        Picture picture = new Picture();
        picture.setId(id);
        picture.setData(("bytes-of-" + id).getBytes(StandardCharsets.UTF_8));
        picture.setContentType("image/jpeg");
        return picture;
    }

    public static CustomUserDetails details(final User user) {
        return new CustomUserDetails(user);
    }

    public static CustomUserDetails details() {
        return details(shelterUser(shelter()));
    }
}
