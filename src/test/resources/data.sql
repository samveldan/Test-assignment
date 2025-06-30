INSERT INTO person (id, username, password, created_at) VALUES
    (1, 'maxim007', '$2a$12$7k14.L6vHE6uwXEotNCvA.q2pY097yLpbLjVe1mBbF0M7Y6EAjPAe', CURRENT_TIMESTAMP),
    (2, 'dimasik228', '$2a$12$/sXsf991/HUcNQIJU3IMNuk3qvCieM5sDPF46Fivf0pflZ4v0cROG', CURRENT_TIMESTAMP),
    (3, 'valera', '$2a$12$2/bT8Wu4Hgg4mZbfidoOauoP9RLLsgCi8x/oiI0NrEGd858uREVMe', CURRENT_TIMESTAMP),
    (4, 'test', '$2a$12$3ZKi0to3pqlE4fZs2BUp7ehHcr2l8W.KSnEezSLFp1.GzURrnVZG6', CURRENT_TIMESTAMP);

INSERT INTO role (id, role_name) VALUES
    (1, 'ROLE_USER'),
    (2, 'ROLE_ADMIN');

INSERT INTO person_role (person_id, role_id)  VALUES
    (1, 1),
    (1, 2),
    (2, 1),
    (3, 1),
    (4, 1),
    (4, 2);

INSERT INTO author (id, name, birth_year) VALUES
    (1, 'Pushkin A.S.', 1799),
    (2, 'Michail Y.L.', 1814),
    (3, 'Tolstoy L.N.', 1828);

INSERT INTO book (id, title, author_id, year, genre) VALUES
    (1, 'Eugene Onegin', 1, 1833, 'POETRY'),
    (2, 'The Captain''s Daughter', 1, 1836, 'HISTORICAL_NOVEL'),

    (3, 'A Hero of Our Time', 2, 1840, 'NOVEL'),
    (4, 'Mtsyri', 2, 1840, 'POEM'),

    (5, 'War and Peace', 3, 1869, 'NOVEL'),
    (6, 'Anna Karenina', 3, 1877, 'NOVEL'),
    (7, 'The Death of Ivan Ilyich', 3, 1886, 'NOVELLA');


-- Корректировка автоинкремента
ALTER TABLE person ALTER COLUMN id RESTART WITH 5;
ALTER TABLE role ALTER COLUMN id RESTART WITH 3;
ALTER TABLE author ALTER COLUMN id RESTART WITH 4;
ALTER TABLE book ALTER COLUMN id RESTART WITH 8;