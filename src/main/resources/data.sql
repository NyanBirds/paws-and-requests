-- INSERTION
INSERT INTO users (first_name, last_name, email, phone_number, password, role, profile_picture) VALUES
('Vegard', 'Eple', 'Vegard@hotmail.com', '12345678', 'verysercurepassword',
 'ADMIN', 'https://i.pinimg.com/474x/f6/51/0c/f6510c7eeab1d3db3b16684244665f7d.jpg'),

('Lisbeth', 'Mango', 'Lisbeth@hotmail.com', '87654321', 'veryunsecurepassword',
 'SHELTER', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR8LqCs6k1rtI_Jh7YmCbTl9woInyTY6skKWtGQZGQ5xEblEN98V38-qBs&s=10'),

('Sara', 'Banana', 'Sara@hotmail.com', '12344321', 'password',
 'USER', 'https://www.reddit.com/media?url=https%3A%2F%2Fpreview.redd.it%2Fcute-duck-in-a-cheerful-yellow-sun-hat-v0-c7xnlawjylhg1.jpeg%3Fwidth%3D640%26crop%3Dsmart%26auto%3Dwebp%26s%3D6f1c966c77d11b6049037b9ad3a0923f99d8a414');

INSERT INTO Animal (name, age, gender, species, user_id) VALUES
('Maja', 2, 'FEMALE', 'DOG', 2),
('John', 4, 'MALE', 'CAT', 3);

INSERT INTO Post (title, description, user_id) VALUES
('GET YOUR PET', 'This dog is looking for a forever home, as she lost her family due to moving.', 2);

INSERT INTO Picture (post_id, url) VALUES
(1, 'https://hips.hearstapps.com/ghk.h-cdn.co/assets/17/30/dachshund.jpg?crop=1.00xw:0.668xh;0,0.260xh');
