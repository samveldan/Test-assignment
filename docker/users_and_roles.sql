CREATE TABLE person (
    id SERIAL PRIMARY KEY,
    username VARCHAR(30) NOT NULL,
    password TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE role (
    id SERIAL PRIMARY KEY,
    role_name VARCHAR(20) NOT NULL
);

CREATE TABLE person_role (
    id SERIAL PRIMARY KEY,
    person_id INT NOT NULL REFERENCES person(id),
    role_id INT NOT NULL REFERENCES role(id),
    UNIQUE (person_id, role_id)
);

INSERT INTO person (username, password, created_at) VALUES
    ('maxim007', '$2a$12$7k14.L6vHE6uwXEotNCvA.q2pY097yLpbLjVe1mBbF0M7Y6EAjPAe', CURRENT_TIMESTAMP),
    ('dimasik228', '$2a$12$/sXsf991/HUcNQIJU3IMNuk3qvCieM5sDPF46Fivf0pflZ4v0cROG', CURRENT_TIMESTAMP),
    ('valera', '$2a$12$2/bT8Wu4Hgg4mZbfidoOauoP9RLLsgCi8x/oiI0NrEGd858uREVMe', CURRENT_TIMESTAMP),
    ('test', '$2a$12$3ZKi0to3pqlE4fZs2BUp7ehHcr2l8W.KSnEezSLFp1.GzURrnVZG6', CURRENT_TIMESTAMP);

INSERT INTO role (role_name) VALUES
    ('ROLE_USER'),
    ('ROLE_ADMIN');

INSERT INTO person_role (person_id, role_id) VALUES
    (1, 1),
    (1, 2),
    (2, 1),
    (3, 1),
    (4, 1),
    (4, 2);