DROP DATABASE IF EXIST pawsandrequest WITH (FORCE);
CREATE DATABASE pawsandrequest;

CREATE TYPE gender_enum AS ENUM ('Male', 'Female');
CREATE TYPE species_enum AS ENUM ('Dog', 'Cat');
CREATE TYPE role_enum AS ENUM ('Admin', 'User', 'Shelter');

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone_number VARCHAR(15),
    role role_enum NOT NULL,
    profile_picture VARCHAR(255),
    org_number VARCHAR(255)
);

CREATE TABLE animal (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    age INT NOT NULL,
    gender gender_enum NOT NULL,
    species species_enum NOT NULL,
    user_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE post (
    id SERIAL PRIMARY KEY,
    title VARCHAR(55) NOT NULL,
    description VARCHAR(255) NOT NULL,
    user_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES  users(id) ON DELETE CASCADE
);


CREATE TABLE pictures (
    id SERIAL PRIMARY KEY ,
    post_id INT NOT NULL,
    url VARCHAR(255) NOT NULL,
    FOREIGN KEY (post_id) REFERENCES post(id) ON DELETE CASCADE
);

-- INSERTION
INSERT INTO users (first_name, last_name, email, phone_number, role, profile_picture) VALUES
('Vegard', 'Eple', 'Vegard@hotmail.com', '12345678',
 'Admin', 'https://i.pinimg.com/474x/f6/51/0c/f6510c7eeab1d3db3b16684244665f7d.jpg'),

('Lisbeth', 'Mango', 'Lisbeth@hotmail.com', '87654321',
 'Shelter', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR8LqCs6k1rtI_Jh7YmCbTl9woInyTY6skKWtGQZGQ5xEblEN98V38-qBs&s=10'),

('Sara', 'Banana', 'Sara@hotmail.com', '12344321',
 'User', 'https://www.reddit.com/media?url=https%3A%2F%2Fpreview.redd.it%2Fcute-duck-in-a-cheerful-yellow-sun-hat-v0-c7xnlawjylhg1.jpeg%3Fwidth%3D640%26crop%3Dsmart%26auto%3Dwebp%26s%3D6f1c966c77d11b6049037b9ad3a0923f99d8a414');

INSERT INTO animals (name, age, gender, species, user_id) VALUES
('Maja', 2, 'Female', 'Dog', 2),
('John', 4, 'Male', 'Cat', 3);

INSERT INTO post (title, description, user_id) VALUES
('GET YOUR PET', 'This dog is looking for a forever home, as she lost her family due to moving.', 2);

INSERT INTO pictures (post_id, url) VALUES
(1, 'https://hips.hearstapps.com/ghk.h-cdn.co/assets/17/30/dachshund.jpg?crop=1.00xw:0.668xh;0,0.260xh');
