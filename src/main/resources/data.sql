-- INSERTION

INSERT INTO Picture (data, content_type) VALUES
    (FILE_READ('classpath:images/maja.jpg'), 'image/jpeg'),
    (FILE_READ('classpath:images/maja2.jpg'), 'image/jpeg'),
    (FILE_READ('classpath:images/dyrebeskyttelsen.jpg'), 'image/jpeg'),
    (FILE_READ('classpath:images/fod.jpg'), 'image/jpeg'),
    (FILE_READ('classpath:images/dyrevernalliansen.jpg'), 'image/jpeg'),
    (FILE_READ('classpath:images/dyreneshus.jpg'), 'image/jpeg');


INSERT INTO Shelter (org_nr, shelter_name, address, description, picture_id)
VALUES
    ('1',
     'Dyrebeskyttelsen Norge',
     'Øvre gate 7, 0551 Oslo',
     'Vi er Norges eldste dyrevernorganisasjon, og jobber for å skaffe hunder og katter i nød et nytt hjem, samtidig som vi kjemper for sterkere dyrerettigheter i hele landet',
     3
    ),
    ('2',
     'FOD – Foreningen for omplassering av dyr',
     'Enebakkveien 866, 1290 Oslo',
     'Vi er en frivillig drevet forening som jobber for å finne nye, kjærlige hjem til forlatte og omplasserte kjæledyr i Oslo-området',
     4
    ),
    ('3',
     'Dyrevern Alliansen',
     'Brenneriveien 7, 0182 Oslo',
     'Vi kombinerer dyrevern med aktiv kamp mot dyremishandling og vanskjøtsel, og jobber for at hver hund og katt skal få en ny sjanse',
     5
    ),
    ('4',
     'Dyrenes Hus',
     'Bekkjarvikveien 1, 5114 Tertnes',
     'Vi tilbyr midlertidig omsorg og formidler adopsjon av hunder og katter i hele Bergensområdet, og hjelper dem med å finne sitt nye hjem for livet',
     6
    );

INSERT INTO users (first_name, last_name, email, phone_number, password, role, org_nr)
VALUES
    ('Vegard', 'Eple', 'vegard@hotmail.com', '12345678', 'verysercurepassword',
     'ADMIN', NULL),

    ('Lisbeth', 'Mango', 'lisbeth@hotmail.com', '87654321', '$2a$10$z6vTdjBFjs5wLnK4Z7rM9OPwCmSL0RGCZTzikbdKCD.fNyB0nNIZq',
     'SHELTERUSER', '1'),

    ('Sara', 'Banana', 'sara@hotmail.com', '12344321', '$2a$10$z6vTdjBFjs5wLnK4Z7rM9OPwCmSL0RGCZTzikbdKCD.fNyB0nNIZq',
     'USER', NULL),

    ('Erik', 'Bjørk', 'Erik@hotmail.com', '99887766', '$2a$10$z6vTdjBFjs5wLnK4Z7rM9OPwCmSL0RGCZTzikbdKCD.fNyB0nNIZq',
     'SHELTERUSER', '2'),

    ('Mari', 'Furu', 'Mari@hotmail.com', '11223344', '$2a$10$z6vTdjBFjs5wLnK4Z7rM9OPwCmSL0RGCZTzikbdKCD.fNyB0nNIZq',
     'SHELTERUSER', '3'),

    ('Ola', 'Gran', 'Ola@hotmail.com', '55667788', '$2a$10$z6vTdjBFjs5wLnK4Z7rM9OPwCmSL0RGCZTzikbdKCD.fNyB0nNIZq',
     'SHELTERUSER', '4');

INSERT INTO Animal (id, name, age, gender, species, org_nr)
VALUES
    ('a1b2c3d4-1111-2222-3333-444455556667', 'Maja', 2, 'FEMALE', 'DOG', '1');

INSERT INTO Post (id, title, description, user_id, animal_id) VALUES
    ('a1b2c3d4-1111-2222-3333-444455556666', 'GET YOUR PET', 'This dog is looking for a forever home, as she lost her family due to moving.', 2, 'a1b2c3d4-1111-2222-3333-444455556667');

INSERT INTO post_picture (post_id, picture_id) VALUES
    ('a1b2c3d4-1111-2222-3333-444455556666', 1),
    ('a1b2c3d4-1111-2222-3333-444455556666', 2);

