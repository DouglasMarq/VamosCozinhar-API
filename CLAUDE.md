# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Test Commands

Java 25 + Spring Boot 3.5 + Gradle 9. A `Makefile` wraps the most common tasks.

- `./gradlew build` — full build; runs `check` which chains `spotlessCheck`, `test`, `integrationTest`. Note: `compileJava` depends on `spotlessCheck`, so formatting violations break compilation.
- `./gradlew spotlessApply` (or `make lint/apply`) — auto-format. Must pass before code compiles.
- `./gradlew spotlessCheck` (or `make lint/check`) — verify formatting only.
- `./gradlew test` — unit tests only. Explicitly excludes `**/*IT.class` and `**/*IntegrationTest.class`.
- `./gradlew integrationTest` — integration tests. Includes only `**/*IT.class` / `**/*IntegrationTest.class`. Must run after `test`.
- `./gradlew test --tests "FullyQualifiedClassName.methodName"` — run a single test.
- `./gradlew buildForProduction` — chains spotless + test + integrationTest + bootJar. Produces `target/app.jar` (non-default location, required by `Dockerfile`).
- `make docker/up` / `make docker/down` — start/stop Postgres 17 + Valkey 8 (Redis-compatible) from `docker-compose.yaml`. Podman equivalents exist.

## Architecture

Standard Spring Boot REST API, layered as `controller → service (interface + `impl`) → repository (wrapper class + `JpaRepository` interface) → entity`. Controllers live under `controller/v1/` and are versioned via the `/v1` path prefix.

Key non-obvious pieces:

- **Repository wrapper pattern.** Each domain has two classes: an `I*Repository` interface extending `JpaRepository` with any custom `@Query` methods, and a `*Repository` `@Repository` class that wraps it. Services depend on the wrapper, not the JPA interface directly. When adding queries, put JPQL/derived queries on the `I*Repository`; put orchestration (e.g. mapping `Limit.of(5)`, unwrapping `Optional`, converting affected-rows to `boolean`) in the wrapper.
- **Redis cache layer (`config/CacheConfig.java`).** Named caches with distinct TTLs: `recipes` (30m), `recipe` (15m), `hotRecipesByViews` (10m). Defined via `RedisCacheManagerBuilderCustomizer`. `RecipesServiceImpl` uses `@Cacheable` / `@CacheEvict` on these names — keep cache names in sync between the config and the annotations. JSON serialization via `GenericJackson2JsonRedisSerializer`, which requires cached entities to be `Serializable` (see `RecipesEntity`).
- **Rate limiting (`annotation/RateLimit.java` + `aspect/RateLimitAspect.java`).** In-memory per-client-IP sliding window using `ConcurrentHashMap<String, ConcurrentLinkedQueue<LocalDateTime>>`. Applied via `@RateLimit` on controller methods. Not distributed — state is per-JVM, so it does not survive restarts or scale across instances. Client IP resolution prefers `X-Forwarded-For`, then `X-Real-IP`, then `request.getRemoteAddr()`.
- **AOP enabled at bootstrap.** `VamoscozinharapiApplication` declares `@EnableAspectJAutoProxy` and `@EnableAsync`; the rate-limit aspect depends on this.
- **JSONB columns via Hibernate.** `RecipesEntity` uses `@JdbcTypeCode(SqlTypes.JSON)` with `columnDefinition = "jsonb"` for `recipe_ingredients` and `prepare`. Postgres-specific — do not swap DB without addressing this.
- **Flyway migrations (`src/main/resources/db/migration`).** A Postgres trigger on `recipes` auto-inserts a row into `hot_recipes` on insert (V1_2). `hot_recipes.recipe_id` is PK + FK with `ON DELETE CASCADE`. Don't manually insert into `hot_recipes` when creating a recipe.
- **`open-in-view: false`.** Lazy associations won't resolve in controllers — keep fetching inside services/repositories.

## Testing

- Integration tests use Testcontainers with the same Postgres 17 + Valkey 8 images as `docker-compose.yaml`. They override Spring properties via `@DynamicPropertySource` and run Flyway against the container.
- Naming convention is load-bearing: a test class ending in `IT.java` or `IntegrationTest.java` is treated as an integration test by the Gradle task split and will be excluded from `./gradlew test`.
- Integration tests require Docker/Podman running locally.

## Profiles & Config

- Default profile (`application.yaml`) has localhost defaults for DB/Redis so `./gradlew bootRun` works against `make docker/up`.
- `application-prod.yaml` (activated by `Dockerfile`'s `-Dspring.profiles.active=prod`) drops defaults for `DB_DATASOURCE`, `DB_PASS`, `DB_USERNAME`, `REDIS_HOST`, `REDIS_PORT` — these must be set as env vars in production.
- `bootJar` writes to `target/app.jar` (not `build/libs/`). The `Dockerfile`'s `COPY target/*.jar app.jar` depends on that.

## CI/CD

- `.github/workflows/ci.yml` — on PRs to `main`: spotless → build → test → integrationTest on a self-hosted runner.
- `.github/workflows/cd.yml` — on push to `main`/`develop`: build, copy `target/app.jar` to `~/jenkins_home/tmp/artifacts` on the self-hosted runner (handoff point to an external deploy pipeline).
