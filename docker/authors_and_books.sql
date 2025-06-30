CREATE TABLE author (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    birth_year INT CHECK (birth_year <= 2025)
);

CREATE TABLE book (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    author_id INT NOT NULL REFERENCES author(id) ON DELETE CASCADE,
    year INT NOT NULL CHECK (year <= 2025),
    genre VARCHAR(50) NOT NULL
);

INSERT INTO author (name, birth_year) VALUES
    ('Pushkin A.S.', 1799),
    ('Michail Y.L.', 1814),
    ('Tolstoy L.N.', 1828);

INSERT INTO book (title, author_id, year, genre) VALUES
    ('Eugene Onegin', 1, 1833, 'POETRY'),
    ('The Captain''s Daughter', 1, 1836, 'HISTORICAL_NOVEL'),

    ('A Hero of Our Time', 2, 1840, 'NOVEL'),
    ('Mtsyri', 2, 1840, 'POEM'),

    ('War and Peace', 3, 1869, 'NOVEL'),
    ('Anna Karenina', 3, 1877, 'NOVEL'),
    ('The Death of Ivan Ilyich', 3, 1886, 'NOVELLA');
