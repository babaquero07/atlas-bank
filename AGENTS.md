# atlas-bank

Spring Boot 4.0.6 / Java 25, hexagonal architecture (ports & adapters).

## Quick start

```bash
# Start Keycloak (required for auth)
docker compose -f docker/docker-compose.yml up -d

# Run the app (H2 in-memory, data lost on restart)
./mvnw spring-boot:run

# Run all tests
./mvnw verify

# Run only unit tests (skip ArchUnit integration tests)
./mvnw test
```

## Architecture

- **`domain/`** — pure Java, zero Spring annotations. Uses Lombok, records, sealed interfaces (state pattern), strategy pattern (fees), chain-of-responsibility (validators).
- **`application/`** — `@Service`, `@Transactional`. Commands/Queries as records. Port interfaces (`port/in/` = use cases, `port/out/` = repository ports). Template method via abstract `TransactionProcessor<C>`.
- **`infrastructure/`** — REST controllers (`@RestController`, `/api/v1/`), JPA adapters (`@Repository` + Spring Data), MapStruct DTO mappers, fraud adapter (simulated), Spring `@EventListener` for domain events, explicit `DomainBeanConfig` wiring domain services.

## Key conventions

- **MapStruct + Lombok** — annotation processors configured in `pom.xml` `maven-compiler-plugin`. Generated mapper impls under `target/generated-sources/`.
- **Constructor injection** via `@RequiredArgsConstructor` everywhere (no field injection).
- **Validation** — Jakarta Bean Validation on DTOs; custom class-level `@DifferentAccounts` constraint.
- **Error responses** — Spring 6 `ProblemDetail` via `@RestControllerAdvice` (400/404/422/500).
- **Caching** — `@EnableCaching` + `@Cacheable("accounts")` on `AccountService.findById`.
- **Auth** — OAuth2 resource server with Keycloak JWT. Roles extracted from `realm_access.roles` in JWT. CSRF disabled.
- **Domain events** — collected in transactions, consumed by `@EventListener` (AuditListener, NotificationListener).

## Testing

- **JUnit 5**, runs with `@SpringBootTest` or pure JUnit 5 for domain tests.
- **ArchUnit** (4 test classes) enforce hexagonal isolation, naming conventions, no cyclic deps, security isolation. Run as part of `verify` lifecycle — slower, need compiled code.
- **Domain tests** (`MoneyDomainTest`, `EmailDomainTest`, `AccountDomainTest`) — pure JUnit 5, no Spring context.
- **Context test** (`BankApplicationTests`) — basic `@SpringBootTest` smoke test.

## Infrastructure

- **Database**: H2 in-memory (`jdbc:h2:mem:atlasbank`), `create-drop` DDL. H2 console at `/h2-console` (dev only).
- **Keycloak**: `docker-compose.yml` in `docker/`. Admin console at `http://localhost:8181`, admin/admin. Realm `atlas-bank` with JWT issuer.
- **Port**: `8080`.
- **No CI, no lint/formatter config, no pre-commit hooks** in this repo.

## Dev commands

| Action | Command |
|--------|---------|
| Build (skip tests) | `./mvnw compile -DskipTests` |
| Run tests | `./mvnw test` |
| Full verify | `./mvnw verify` |
| Run app | `./mvnw spring-boot:run` |
| Package JAR | `./mvnw package -DskipTests` |
