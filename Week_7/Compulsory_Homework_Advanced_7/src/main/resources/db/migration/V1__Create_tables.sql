-- Genres table
CREATE TABLE genres (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) UNIQUE NOT NULL
);

-- Actors table
CREATE TABLE actors (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    birth_date DATE
);

-- Movies table
CREATE TABLE movies (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    release_date DATE,
    duration INT,
    score FLOAT
);

-- Junction table for movie-genre
CREATE TABLE movie_genres (
    movie_id INT,
    genre_id INT,
    PRIMARY KEY (movie_id, genre_id),
    FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genres(id) ON DELETE CASCADE
);

-- Junction table for movie-actor
CREATE TABLE movie_actors (
    movie_id INT,
    actor_id INT,
    PRIMARY KEY (movie_id, actor_id),
    FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE,
    FOREIGN KEY (actor_id) REFERENCES actors(id) ON DELETE CASCADE
);

-- Movie lists table
CREATE TABLE movie_lists (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    creation_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Junction table for movie list-movies
CREATE TABLE movie_list_movies (
    list_id INT,
    movie_id INT,
    PRIMARY KEY (list_id, movie_id),
    FOREIGN KEY (list_id) REFERENCES movie_lists(id) ON DELETE CASCADE,
    FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE
);

-- View for all movies with genres and actors
CREATE VIEW movie_details AS
SELECT
    m.id,
    m.title,
    m.release_date,
    m.duration,
    m.score,
    GROUP_CONCAT(DISTINCT g.name) AS genres,
    GROUP_CONCAT(DISTINCT a.name) AS actors
FROM movies m
LEFT JOIN movie_genres mg ON m.id = mg.movie_id
LEFT JOIN genres g ON mg.genre_id = g.id
LEFT JOIN movie_actors ma ON m.id = ma.movie_id
LEFT JOIN actors a ON ma.actor_id = a.id
GROUP BY m.id, m.title, m.release_date, m.duration, m.score;