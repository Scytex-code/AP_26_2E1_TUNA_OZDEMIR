# Movie Spring Boot Application

Lab 7 requirements were added on top of the existing Week 7 project. The application now keeps the original movie/actor/genre domain and adds a Spring Boot REST API, JWT security, Swagger documentation, CSV bootstrap, HTML reporting, and a Choco-Solver based advanced endpoint.

## Covered Requirements

### Compulsory
- Spring Boot project
- REST controller for retrieving the movie list with `GET /api/movies`

### Homework
- `POST /api/movies`
- `PUT /api/movies/{id}`
- `PATCH /api/movies/{id}/score`
- `DELETE /api/movies/{id}`
- Global exception handler
- Simple Spring Boot client using `RestTemplate`
- Swagger UI documentation

### Advanced
- Solver endpoint for unrelated movies with size greater than a given parameter:
  `GET /api/advanced/movies/unrelated?minSize=2`
- JWT security for the API

### Existing Project Features Kept
- Flyway migrations
- MySQL persistence
- CSV bootstrap import from `movies.csv`
- HTML report generation with FreeMarker via `GET /api/reports/movies/html`

## Tech Stack
- Spring Boot 2.7
- Spring Web
- Spring Data JPA
- Spring Security + JWT
- Flyway
- MySQL
- FreeMarker
- OpenCSV
- Choco-Solver
- springdoc-openapi

## Default Credentials
- Username: `apiuser`
- Password: `password`

These can be changed with environment variables:
- `API_USERNAME`
- `API_PASSWORD`

## Database Configuration
Default values:

- `DB_URL=jdbc:mysql://localhost:3306/movie_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC`
- `DB_USER=root`
- `DB_PASSWORD=root123`

## Run

```bash
mvn test
mvn spring-boot:run
```

If you want to disable CSV bootstrap on startup:

```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--movie.bootstrap.enabled=false
```

## Swagger

After startup:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Authentication Flow

1. Call `POST /api/auth/login`
2. Send:

```json
{
  "username": "apiuser",
  "password": "password"
}
```

3. Copy the returned JWT token
4. Use `Authorization: Bearer <token>` for secured endpoints

## Main Endpoints

### Movies
- `GET /api/movies`
- `GET /api/movies/{id}`
- `POST /api/movies`
- `PUT /api/movies/{id}`
- `PATCH /api/movies/{id}/score`
- `DELETE /api/movies/{id}`

### Actors
- `GET /api/actors`
- `GET /api/actors/{id}`
- `POST /api/actors`
- `PUT /api/actors/{id}`
- `DELETE /api/actors/{id}`

### Genres
- `GET /api/genres`
- `GET /api/genres/{id}`
- `POST /api/genres`
- `PUT /api/genres/{id}`
- `DELETE /api/genres/{id}`

### Advanced
- `GET /api/advanced/movies/unrelated?minSize=2`

This returns a saved `movie_lists` entry whose movies do not share actors and whose size is strictly greater than `minSize`.

### Reports
- `GET /api/reports/movies/html`

## Example Movie Payload

```json
{
  "title": "Arrival",
  "releaseDate": "2016-11-11",
  "duration": 116,
  "score": 8.1,
  "genreIds": [1, 2],
  "actorIds": [1, 3]
}
```

## Simple Client

A simple Spring client runner exists in the project. It is disabled by default.

Enable it with:

```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--movie.client.enabled=true
```

Optional client settings:
- `MOVIE_CLIENT_BASE_URL`
- `MOVIE_CLIENT_USERNAME`
- `MOVIE_CLIENT_PASSWORD`
