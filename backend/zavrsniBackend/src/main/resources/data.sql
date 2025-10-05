
INSERT INTO users (email, user_name, password, first_name, last_name, provider, image_url)
VALUES ('zvonko_krivaja@gmail.com', 'zvonko_krivaja', '$2a$10$HfvLuDn/a29ILbOHXpIjVun1jUSD1QI3213Xi9U7ZE3oUPLPLfwDW', 'Zvonko', 'Krivaja', 'LOCAL', '/uploads/pfp1.jpg');
INSERT INTO users (email, user_name, password, first_name, last_name, provider, image_url)
VALUES ('ivana_babić@gmail.com', 'ivana_babić', '$2a$10$7kLmDn/a29ILbOHXpIjVun1jUSD1QI3213Xi9U7ZE3oUPLPLfwXY', 'Ivana', 'Babić', 'LOCAL', '/uploads/pfp1.jpg');
INSERT INTO users (email, user_name, password, first_name, last_name, provider, image_url)
VALUES ('dario_majstorović@gmail.com', 'dario_majstorović', '$2a$10$3hNvDn/a29ILbOHXpIjVun1jUSD1QI3213Xi9U7ZE3oUPLPLfwYZ', 'Dario', 'Majstorović', 'LOCAL', '/uploads/pfp1.jpg');


INSERT INTO post (text, image_url, created_at, author_id) VALUES
                                                              ('Moj prvi post!', '/uploads/slika1.jpg', CURRENT_TIMESTAMP, 1),
                                                              ('Drugi post s tekstom i slikom.', '/uploads/slika2.jpg', CURRENT_TIMESTAMP, 2),
                                                              ('Treći post s tekstom i slikom.', '/uploads/slika1.jpg', CURRENT_TIMESTAMP, 3);