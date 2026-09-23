-- INSERTION

INSERT INTO Shelter (org_nr, shelter_name, address, description, logo)
VALUES
    ('1',
     'Dyrebeskyttelsen Norge',
     'Øvre gate 7, 0551 Oslo',
     'Vi er Norges eldste dyrevernorganisasjon, og jobber for å skaffe hunder og katter i nød et nytt hjem, samtidig som vi kjemper for sterkere dyrerettigheter i hele landet',
     'https://scontent.fsvg1-1.fna.fbcdn.net/v/t39.30808-6/391587480_705360571628818_6774951360565198877_n.jpg?stp=dst-jpg_tt6&cstp=mx960x960&ctp=s960x960&_nc_cat=108&_nc_map=urlgen_bucketless&ccb=1-7&_nc_sid=6ee11a&_nc_ohc=Xm5A71dZ_C0Q7kNvwFJRDfB&_nc_oc=AdqWuAKoiSB4DXKhIvZ4JtVQvAS_fb2xfHdktloffsTC9zb1k0icCdsbhz259oi4pTU&_nc_zt=23&_nc_ht=scontent.fsvg1-1.fna&_nc_gid=PyNy4u67ynGLnewOdWkMfA&_nc_ss=7b289&oh=00_AQIhrUZ2HiPNgRz_H14zyAlANLH19Q5iN69ts9Reohriow&oe=6AB2ECF5'
    ),
    ('2',
     'FOD – Foreningen for omplassering av dyr',
     'Enebakkveien 866, 1290 Oslo', 'Vi er en frivillig drevet forening som jobber for å finne nye, kjærlige hjem til forlatte og omplasserte kjæledyr i Oslo-området',
     'https://scontent.fsvg1-1.fna.fbcdn.net/v/t39.30808-6/391763790_721124743379291_8810608562810801027_n.jpg?stp=dst-jpg_tt6&cstp=mx1004x929&ctp=s1004x929&_nc_cat=109&_nc_map=urlgen_bucketless&ccb=1-7&_nc_sid=6ee11a&_nc_ohc=WAWvms8r-G8Q7kNvwGkUYS3&_nc_oc=AdqZZ7zslpv789LjmygJJmVY7akE98CtZAN2cnI-I-bqH8q317MA-PndtDr3Q0RFewc&_nc_zt=23&_nc_ht=scontent.fsvg1-1.fna&_nc_gid=20aNM7HBmd3l81ytbdDhsQ&_nc_ss=7b289&oh=00_AQK84lbs1TaHf3jcINxAbHEV9tiK9DcEPX1lI_XqEfTkUQ&oe=6AB2D769'
    ),
    ('3',
     'Dyrevern Alliansen',
     'Brenneriveien 7, 0182 Oslo',
     'Vi kombinerer dyrevern med aktiv kamp mot dyremishandling og vanskjøtsel, og jobber for at hver hund og katt skal få en ny sjanse',
     'https://dyrevern.no/content/uploads/2019/12/Logo-Dyrevernalliansen-Positiv-RGB-uten-tekst.jpg'
    ),
    ('4',
     'Dyrenes Hus',
     'Bekkjarvikveien 1, 5114 Tertnes',
     'Vi tilbyr midlertidig omsorg og formidler adopsjon av hunder og katter i hele Bergensområdet, og hjelper dem med å finne sitt nye hjem for livet',
     'https://scontent.fsvg1-1.fna.fbcdn.net/v/t39.30808-6/279102315_285955710406287_2440804527009277289_n.jpg?stp=dst-jpg_tt6&cstp=mx908x908&ctp=s908x908&_nc_cat=108&_nc_map=urlgen_bucketless&ccb=1-7&_nc_sid=6ee11a&_nc_ohc=Bvfb0SvdzOsQ7kNvwEOlBI0&_nc_oc=AdqjDuCSb3w-V_C2X7knWvp27NwisgHbWD-eyL2OgDaW4SXUBjeCDXAaEfPgw2hWRKA&_nc_zt=23&_nc_ht=scontent.fsvg1-1.fna&_nc_gid=olFh1a0DkNSdL15HRZnRtw&_nc_ss=7b289&oh=00_AQJn_VOKGmc6slxa2lmBZUGFXm0sKbsoBDyDgNIqe5rzXA&oe=6AB2FA47'
    );

INSERT INTO users (first_name, last_name, email, phone_number, password, role, profile_picture, org_nr)
VALUES
    ('Vegard', 'Eple', 'vegard@hotmail.com', '12345678', 'verysercurepassword',
     'ADMIN', 'https://i.pinimg.com/474x/f6/51/0c/f6510c7eeab1d3db3b16684244665f7d.jpg', NULL),

    ('Lisbeth', 'Mango', 'lisbeth@hotmail.com', '87654321', '$2a$10$z6vTdjBFjs5wLnK4Z7rM9OPwCmSL0RGCZTzikbdKCD.fNyB0nNIZq',
     'SHELTERUSER', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR8LqCs6k1rtI_Jh7YmCbTl9woInyTY6skKWtGQZGQ5xEblEN98V38-qBs&s=10','1'),

    ('Sara', 'Banana', 'sara@hotmail.com', '12344321', '$2a$10$z6vTdjBFjs5wLnK4Z7rM9OPwCmSL0RGCZTzikbdKCD.fNyB0nNIZq',
     'USER', 'https://www.reddit.com/media?url=https%3A%2F%2Fpreview.redd.it%2Fcute-duck-in-a-cheerful-yellow-sun-hat-v0-c7xnlawjylhg1.jpeg%3Fwidth%3D640%26crop%3Dsmart%26auto%3Dwebp%26s%3D6f1c966c77d11b6049037b9ad3a0923f99d8a414', NULL);

    ('Erik', 'Bjørk', 'Erik@hotmail.com', '99887766', '$2a$10$z6vTdjBFjs5wLnK4Z7rM9OPwCmSL0RGCZTzikbdKCD.fNyB0nNIZq',
     'SHELTERUSER', 'https://i.pinimg.com/474x/8a/8f/ee/8a8fee6a1a1a1a1a1a1a1a1a1a1a1a1a.jpg', '2'),

    ('Mari', 'Furu', 'Mari@hotmail.com', '11223344', '$2a$10$z6vTdjBFjs5wLnK4Z7rM9OPwCmSL0RGCZTzikbdKCD.fNyB0nNIZq',
     'SHELTERUSER', 'https://i.pinimg.com/474x/55/6f/4c/556f4c1e2f3a4b5c6d7e8f9a0b1c2d3e.jpg', '3'),

    ('Ola', 'Gran', 'Ola@hotmail.com', '55667788', '$2a$10$z6vTdjBFjs5wLnK4Z7rM9OPwCmSL0RGCZTzikbdKCD.fNyB0nNIZq',
     'SHELTERUSER', 'https://i.pinimg.com/474x/12/34/56/1234567890abcdef1234567890abcdef.jpg', '4');

INSERT INTO Animal (id, name, age, gender, species, org_nr)
VALUES
    ('a1b2c3d4-1111-2222-3333-444455556667', 'Maja', 2, 'FEMALE', 'DOG', '1'),
    ('a1b2c3d4-1111-2222-3333-444455556677', 'John', 4, 'MALE', 'CAT', '1'),
    ('a1b2c3d4-1111-9999-0000-000000000001', 'Luna', 3, 'FEMALE', 'CAT', '1'),
    ('a1b2c3d4-1111-9999-0000-000000000002', 'Milo', 1, 'MALE', 'DOG', '1'),
    ('a1b2c3d4-1111-9999-0000-000000000003', 'Bella', 5, 'FEMALE', 'DOG', '1'),
    ('a1b2c3d4-2222-9999-0000-000000000001', 'Charlie', 2, 'MALE', 'CAT', '2'),
    ('a1b2c3d4-2222-9999-0000-000000000002', 'Daisy', 4, 'FEMALE', 'DOG', '2'),
    ('a1b2c3d4-2222-9999-0000-000000000003', 'Rocky', 6, 'MALE', 'DOG', '2'),
    ('a1b2c3d4-2222-9999-0000-000000000004', 'Willow', 1, 'FEMALE', 'CAT', '2'),
    ('a1b2c3d4-3333-9999-0000-000000000001', 'Oscar', 3, 'MALE', 'DOG', '3'),
    ('a1b2c3d4-3333-9999-0000-000000000002', 'Nala', 2, 'FEMALE', 'CAT', '3'),
    ('a1b2c3d4-3333-9999-0000-000000000003', 'Toby', 7, 'MALE', 'CAT', '3'),
    ('a1b2c3d4-4444-9999-0000-000000000001', 'Sasha', 3, 'FEMALE', 'DOG', '4'),
    ('a1b2c3d4-4444-9999-0000-000000000002', 'Finn', 2, 'MALE', 'CAT', '4'),
    ('a1b2c3d4-4444-9999-0000-000000000003', 'Molly', 8, 'FEMALE', 'DOG', '4'),
    ('a1b2c3d4-4444-9999-0000-000000000004', 'Simba', 4, 'MALE', 'CAT', '4');

INSERT INTO Post (id, title, description, user_id, animal_id) VALUES
    ('a1b2c3d4-1111-2222-3333-444455556666', 'GET YOUR PET', 'This dog is looking for a forever home, as she lost her family due to moving.', 2, 'a1b2c3d4-1111-2222-3333-444455556667'),
    ('b1c2d3e4-1111-9999-0000-000000000001', 'Sensitive soulmate', 'John is a calm and gentle tomcat who loves sitting by the window. He is looking for a quiet home without small children.', 2, 'a1b2c3d4-1111-2222-3333-444455556677'),
    ('b1c2d3e4-1111-9999-0000-000000000002', 'Playful Luna', 'Luna is a curious kitten full of energy. She loves toy mice and needs a patient owner who can keep up with her.', 2, 'a1b2c3d4-1111-9999-0000-000000000001'),
    ('b1c2d3e4-1111-9999-0000-000000000003', 'Milo wants a home', 'This young pup is full of joy and already knows basic commands. He would thrive in an active family.', 2, 'a1b2c3d4-1111-9999-0000-000000000002'),
    ('b1c2d3e4-1111-9999-0000-000000000004', 'Sweet Bella', 'Bella is a loyal senior who was surrendered by her former owner. She deserves a calm retirement home.', 2, 'a1b2c3d4-1111-9999-0000-000000000003'),
    ('b1c2d3e4-2222-9999-0000-000000000001', 'Charlie says hello', 'Charlie is a social cat who gets along with other cats and dogs. He was rescued from the street and loves cuddles.', 4, 'a1b2c3d4-2222-9999-0000-000000000001'),
    ('b1c2d3e4-2222-9999-0000-000000000002', 'Daisy the explorer', 'Daisy enjoys long walks and exploring new smells. A garden would be the dream for this friendly girl.', 4, 'a1b2c3d4-2222-9999-0000-000000000002'),
    ('b1c2d3e4-2222-9999-0000-000000000003', 'Big softie Rocky', 'Despite his size, Rocky is a gentle giant who only wants belly rubs and a warm sofa to nap on.', 4, 'a1b2c3d4-2222-9999-0000-000000000003'),
    ('b1c2d3e4-2222-9999-0000-000000000004', 'Tiny Willow', 'Willow is a shy kitten who needs time to trust. She bonds deeply once she feels safe and loves her foster owner.', 4, 'a1b2c3d4-2222-9999-0000-000000000004'),
    ('b1c2d3e4-3333-9999-0000-000000000001', 'Oscar the gentleman', 'Oscar is a well-mannered dog who walks beautifully on a leash. He is looking for a first-time dog owner to spoil him.', 5, 'a1b2c3d4-3333-9999-0000-000000000001'),
    ('b1c2d3e4-3333-9999-0000-000000000002', 'Gorgeous Nala', 'Nala is a striking tabby with a heart of gold. She is happiest when curled up on a lap and purring.', 5, 'a1b2c3d4-3333-9999-0000-000000000002'),
    ('b1c2d3e4-3333-9999-0000-000000000003', 'Senior Toby', 'Toby spent years on the streets and now wants a warm bed to call his own. He is very affectionate once he trusts.', 5, 'a1b2c3d4-3333-9999-0000-000000000003'),
    ('b1c2d3e4-4444-9999-0000-000000000001', 'Sasha needs you', 'Sasha was abandoned at the shelter and is recovering from neglect. She is sweet as can be and ready for a fresh start.', 6, 'a1b2c3d4-4444-9999-0000-000000000001'),
    ('b1c2d3e4-4444-9999-0000-000000000002', 'Finn the adventurer', 'Finn is active outdoors but calm indoors. He loves walking in the forest and watching birds from the window.', 6, 'a1b2c3d4-4444-9999-0000-000000000002'),
    ('b1c2d3e4-4444-9999-0000-000000000003', 'Gentle Molly', 'Molly is an older girl who has raised several litters. Now it is her turn to be pampered in a peaceful home.', 6, 'a1b2c3d4-4444-9999-0000-000000000003'),
    ('b1c2d3e4-4444-9999-0000-000000000004', 'Handsome Simba', 'Simba is a majestic long-haired cat who loves being brushed. He is looking for a home where he gets lots of attention.', 6, 'a1b2c3d4-4444-9999-0000-000000000004');

INSERT INTO Picture (post_id, data, content_type) VALUES
    ('a1b2c3d4-1111-2222-3333-444455556666', FILE_READ('classpath:images/maja.jpg'), 'image/jpeg'),
    ('a1b2c3d4-1111-2222-3333-444455556666', FILE_READ('classpath:images/maja2.jpg'), 'image/jpeg');

INSERT INTO Picture (post_id, url) VALUES
    ('a1b2c3d4-1111-2222-3333-444455556666', 'https://hips.hearstapps.com/ghk.h-cdn.co/assets/17/30/dachshund.jpg?crop=1.00xw:0.668xh;0,0.260xh'),
    ('b1c2d3e4-1111-9999-0000-000000000001', 'https://thumb.wikimedia.org/wikipedia/commons/thumb/2/25/Siam_lilacpoint.jpg/960px-Siam_lilacpoint.jpg'),
    ('b1c2d3e4-1111-9999-0000-000000000002', 'https://thumb.wikimedia.org/wikipedia/commons/thumb/b/bc/Juvenile_Ragdoll.jpg/960px-Juvenile_Ragdoll.jpg'),
    ('b1c2d3e4-1111-9999-0000-000000000003', 'https://placedog.net/640/480?id=1'),
    ('b1c2d3e4-1111-9999-0000-000000000004', 'https://placedog.net/640/480?id=2'),
    ('b1c2d3e4-2222-9999-0000-000000000001', 'https://thumb.wikimedia.org/wikipedia/commons/thumb/2/2d/Mystica_from_British_Empire_Cattery.jpg/960px-Mystica_from_British_Empire_Cattery.jpg'),
    ('b1c2d3e4-2222-9999-0000-000000000002', 'https://placedog.net/640/480?id=3'),
    ('b1c2d3e4-2222-9999-0000-000000000003', 'https://placedog.net/640/480?id=4'),
    ('b1c2d3e4-2222-9999-0000-000000000004', 'https://thumb.wikimedia.org/wikipedia/commons/thumb/4/4d/Cat_November_2010-1a.jpg/960px-Cat_November_2010-1a.jpg'),
    ('b1c2d3e4-3333-9999-0000-000000000001', 'https://placedog.net/640/480?id=5'),
    ('b1c2d3e4-3333-9999-0000-000000000002', 'https://upload.wikimedia.org/wikipedia/commons/8/87/M%C3%A2le_Black_Silver_Blotched_Tabby.jpeg'),
    ('b1c2d3e4-3333-9999-0000-000000000003', 'https://upload.wikimedia.org/wikipedia/commons/8/81/Persialainen.jpg'),
    ('b1c2d3e4-4444-9999-0000-000000000001', 'https://placedog.net/640/480?id=6'),
    ('b1c2d3e4-4444-9999-0000-000000000002', 'https://upload.wikimedia.org/wikipedia/commons/6/64/Ragdoll_from_Gatil_Ragbelas.jpg'),
    ('b1c2d3e4-4444-9999-0000-000000000003', 'https://placedog.net/640/480?id=7'),
    ('b1c2d3e4-4444-9999-0000-000000000004', 'https://upload.wikimedia.org/wikipedia/commons/1/16/Siamese_cat_Vaillante.JPG');
