Catalog Service
================

Spring Boot microservice for managing a book catalog. It exposes a REST API backed
by a PostgreSQL database and uses Flyway for schema migrations.

Features
--------
- CRUD operations for books
- Validation for ISBN/title/author
- JDBC auditing for created/updated timestamps
- OpenAPI UI via Springdoc

API Endpoints
-------------
- `GET /books` list all books
- `GET /books/{isbn}` get a book by ISBN
- `POST /books` create a book
- `PUT /books/{isbn}` update a book
- `DELETE /books/{isbn}` delete a book
- `GET /` return the greeting message

Data Model
----------
Book fields:
- `isbn` (10 or 13 digits, required)
- `title` (required)
- `author` (required)
- `price`
- `publisher`

Local Development
-----------------
1. Start a PostgreSQL instance (set credentials via environment or config):
   - database: `polardb_catalog`
2. Run the service:
   - `./gradlew bootRun`
3. Service is available at `http://localhost:9001`
4. OpenAPI UI: `http://localhost:9001/swagger-ui/index.html`

Configuration
-------------
Edit `src/main/resources/application.yml` to adjust:
- Server port
- Database connection
- Config server settings
- Greeting message (`polar.greeting`)

Test Data
---------
Run with the `testdata` profile to preload sample books:
- `./gradlew bootRun --args='--spring.profiles.active=testdata'`

Testing
-------
- `./gradlew test`

Notes
-----
Integration tests use Testcontainers with the `integration` profile defined in
`src/test/resources/application-integration.yml`.
