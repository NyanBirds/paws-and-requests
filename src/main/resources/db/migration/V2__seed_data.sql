-- Seed data for Paws and Requests: four real Norwegian shelters, the demo
-- users, their animals, the adoption posts and the adoption requests.
--
-- All ids are fixed so the data is byte-for-byte reproducible across machines.
--
-- Every seeded account uses the same password: password
-- See README.md for the credentials table.
--
-- picture.data holds a 1x1 placeholder JPEG. The real images live in
-- src/main/resources/images and are written by SeedPictures on startup,
-- because binary image data does not belong in a migration file.
--
-- Picture ids 9, 12, 13, 15 and 16 were uploaded by hand through the app and
-- have no counterpart in src/main/resources/images, so SeedPictures serves a
-- repository image for them instead. The rows and their links are unchanged.

-- Pictures -----------------------------------------------------------------
-- The base64 below is a 408 byte 1x1 JPEG, used only so picture.data satisfies
-- its NOT NULL constraint until SeedPictures replaces it.
INSERT INTO picture (id, data, content_type)
SELECT seeded.id,
       decode('/9j/4AAQSkZJRgABAQEAYABgAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRof'
           || 'Hh0aHBwcJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPDU0NP/bAEMBCQkJDAsMGA0NGDIhHCEyMjIyMjIyMjIy'
           || 'MjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAABAAEDASIAAhEBAxEB/8QAHw'
           || 'AAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUE'
           || 'GE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNk'
           || 'ZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NX'
           || 'W19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/9oACAEBAAA/APn+iiigD//Z', 'base64'),
       'image/jpeg'
FROM (VALUES (1), (2), (4), (5), (6), (7), (9), (12), (13), (15), (16)) AS seeded(id);

-- The ids above are inserted explicitly, so the identity sequence has to be
-- moved past them. Without this the first picture added through the app would
-- collide on the primary key.
SELECT setval('picture_id_seq', (SELECT MAX(id) FROM picture));

-- Shelters -----------------------------------------------------------------
INSERT INTO shelter (org_nr, shelter_name, address, description, picture_id)
VALUES
    ('1',
     'Dyrebeskyttelsen Norge',
     'Øvre gate 7, 0551 Oslo',
     'Vi er Norges eldste dyrevernorganisasjon, og jobber for å skaffe hunder og katter i nød et nytt hjem, samtidig som vi kjemper for sterkere dyrerettigheter i hele landet',
     4),
    ('2',
     'FOD – Foreningen for omplassering av dyr',
     'Enebakkveien 866, 1290 Oslo',
     'Vi er en frivillig drevet forening som jobber for å finne nye, kjærlige hjem til forlatte og omplasserte kjæledyr i Oslo-området',
     7),
    ('3',
     'Dyrevern Alliansen',
     'Brenneriveien 7, 0182 Oslo',
     'Vi kombinerer dyrevern med aktiv kamp mot dyremishandling og vanskjøtsel, og jobber for at hver hund og katt skal få en ny sjanse',
     6),
    ('4',
     'Dyrenes Hus',
     'Bekkjarvikveien 1, 5114 Tertnes',
     'Vi tilbyr midlertidig omsorg og formidler adopsjon av hunder og katter i hele Bergensområdet, og hjelper dem med å finne sitt nye hjem for livet',
     5);

-- Users --------------------------------------------------------------------
INSERT INTO users (id, first_name, last_name, email, phone_number, password, role, org_nr)
VALUES
    ('a1b2c3d4-1111-2222-3333-444455556660', 'Vegard', 'Eple', 'vegard@hotmail.com', '12345678',
     '$2a$10$z6vTdjBFjs5wLnK4Z7rM9OPwCmSL0RGCZTzikbdKCD.fNyB0nNIZq', 'ADMIN', NULL),
    ('a1b2c3d4-1111-2222-3333-444455556661', 'Lisbeth', 'Mango', 'lisbeth@hotmail.com', '87654321',
     '$2a$10$z6vTdjBFjs5wLnK4Z7rM9OPwCmSL0RGCZTzikbdKCD.fNyB0nNIZq', 'SHELTERUSER', '1'),
    ('a1b2c3d4-1111-2222-3333-444455556662', 'Sara', 'Banana', 'sara@hotmail.com', '12344321',
     '$2a$10$z6vTdjBFjs5wLnK4Z7rM9OPwCmSL0RGCZTzikbdKCD.fNyB0nNIZq', 'USER', NULL);

-- Animals ------------------------------------------------------------------
INSERT INTO animal (id, name, age, gender, species, org_nr) VALUES
    ('09fe46bc-d1ee-4d60-81f7-1002d5007c73', 'Maja', 1, 'FEMALE', 'DOG', '1'),
    ('ad6b26d9-6af6-46ed-9400-a0e91d6c2d05', 'Pompel', 5, 'MALE', 'DOG', '1'),
    ('33573fce-9a7f-483f-a438-db19467a43a6', 'Mons', 10, 'MALE', 'CAT', '1'),
    ('5543d493-91d6-4722-9cae-e6c56d022cfe', 'Karl', 2, 'MALE', 'DOG', '1'),
    ('6b062b7a-f772-4632-91b9-72168f5f4f51', 'Bob', 5, 'MALE', 'DOG', '1');

-- Posts --------------------------------------------------------------------
INSERT INTO post (id, title, description, user_id, animal_id) VALUES
    ('5a77b8d1-963a-46ee-8d17-e4d7d67dadb9', 'Beautiful Maja is looking for a forever home!',
     'Maja is a sweet little girl, found in the streets. She loves to play fetch and has a very good apetite!',
     'a1b2c3d4-1111-2222-3333-444455556661', '09fe46bc-d1ee-4d60-81f7-1002d5007c73'),
    ('bf0c2a97-2bb6-437d-a85b-de1ee258ca66', 'Bob the Dog',
     'Bob is a chill dog, loves to sit on the sofa and watch tv!',
     'a1b2c3d4-1111-2222-3333-444455556661', '6b062b7a-f772-4632-91b9-72168f5f4f51');

INSERT INTO post_picture (post_id, picture_id) VALUES
    ('5a77b8d1-963a-46ee-8d17-e4d7d67dadb9', 16),
    ('bf0c2a97-2bb6-437d-a85b-de1ee258ca66', 15);

-- Adoption forms -----------------------------------------------------------
INSERT INTO adoption_form (id, content, user_id, post_id) VALUES
    ('5cd28c7f-710a-42f3-862e-0f3be9784015',
     'Hello! I would really like to adopt maja! I have a dog already named Bob and he is very kind! Would love a little friend for him! Give me a call if you are interested!',
     'a1b2c3d4-1111-2222-3333-444455556662', '5a77b8d1-963a-46ee-8d17-e4d7d67dadb9');
