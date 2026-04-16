# Vamos Cozinhar API

Small Spring Boot REST API that serves recipes to a front-end.

Stack: Java 25, Spring Boot 3.5, Postgres 17, Valkey 8 (Redis-compatible), Flyway, Gradle 9.

## Endpoints

- `GET /v1/recipes` — list all recipes
- `GET /v1/recipes/{id}` — get one recipe
- `POST /v1/recipes` — create recipe (rate-limited)
- `DELETE /v1/recipes/{id}` — delete recipe (rate-limited)
- `GET /v1/hotrecipes/hot/views` — top 5 by views
- `GET /v1/hotrecipes/hot/likes` — top 5 by likes
- `PATCH /v1/hotrecipes/hot/{id}` — register view / like / dislike (rate-limited)

Actuator: `/actuator/health`, `/actuator/metrics`, `/actuator/prometheus`.

## Run locally

```
make docker/up           # start Postgres + Valkey
./gradlew bootRun
```

## Test

```
./gradlew test              # unit
./gradlew integrationTest   # Testcontainers (needs Docker running)
```

## Build production jar

```
./gradlew buildForProduction   # writes target/app.jar
```

See `CLAUDE.md` for architecture notes.
