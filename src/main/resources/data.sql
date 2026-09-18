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
    ('Vegard', 'Eple', 'Vegard@hotmail.com', '12345678', 'verysercurepassword',
     'ADMIN', 'https://i.pinimg.com/474x/f6/51/0c/f6510c7eeab1d3db3b16684244665f7d.jpg', NULL),

    ('Lisbeth', 'Mango', 'Lisbeth@hotmail.com', '87654321', '$2a$10$z6vTdjBFjs5wLnK4Z7rM9OPwCmSL0RGCZTzikbdKCD.fNyB0nNIZq',
     'SHELTERUSER', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR8LqCs6k1rtI_Jh7YmCbTl9woInyTY6skKWtGQZGQ5xEblEN98V38-qBs&s=10','1'),

    ('Sara', 'Banana', 'Sara@hotmail.com', '12344321', '$2a$10$z6vTdjBFjs5wLnK4Z7rM9OPwCmSL0RGCZTzikbdKCD.fNyB0nNIZq',
     'USER', 'https://www.reddit.com/media?url=https%3A%2F%2Fpreview.redd.it%2Fcute-duck-in-a-cheerful-yellow-sun-hat-v0-c7xnlawjylhg1.jpeg%3Fwidth%3D640%26crop%3Dsmart%26auto%3Dwebp%26s%3D6f1c966c77d11b6049037b9ad3a0923f99d8a414', NULL);

INSERT INTO Animal (id, name, age, gender, species, org_nr)
VALUES
    ('a1b2c3d4-1111-2222-3333-444455556667', 'Maja', 2, 'FEMALE', 'DOG', '1'),
    ('a1b2c3d4-1111-2222-3333-444455556677', 'John', 4, 'MALE', 'CAT', '1');

INSERT INTO Post (id, title, description, user_id, animal_id) VALUES
    ('a1b2c3d4-1111-2222-3333-444455556666', 'GET YOUR PET', 'This dog is looking for a forever home, as she lost her family due to moving.', 2, 'a1b2c3d4-1111-2222-3333-444455556667');

INSERT INTO Picture (post_id, url) VALUES
    ('a1b2c3d4-1111-2222-3333-444455556666', 'https://hips.hearstapps.com/ghk.h-cdn.co/assets/17/30/dachshund.jpg?crop=1.00xw:0.668xh;0,0.260xh');
