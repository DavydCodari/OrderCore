# OrderCore

## Product listings

- `GET /product` lists visible products and is available without authentication.
- `GET /product/admin` lists all products, including hidden ones, and requires the admin role.

Both endpoints accept zero-based `page` and `size` query parameters (for example,
`/product?page=0&size=20`) and return a page with product data and pagination
metadata. Products are ordered by ID; custom sorting is not supported.

## Tests and coverage

Run `./gradlew test` (Windows: `.\gradlew.bat test`) to execute the unit tests
and generate a JaCoCo report. Open `build/reports/jacoco/test/html/index.html`
to view coverage. An XML report is also generated in `build/reports/jacoco/test/`,
and the pull request workflow uploads both reports as an artifact.

## Local database setup

1. Run PostgreSQL on `localhost:5432` and create a database named `order_core`.
2. Copy `.env.example` to `.env` if `.env` does not already exist.
3. Set `DB_PASSWORD` in `.env` to the password for your local `postgres` user.
4. Run `./gradlew bootRun` (Windows: `.\gradlew.bat bootRun`) from the project root.

Spring imports the root `.env` file as Java properties. Use `DB_PASSWORD=your-password`
without shell-style quotes; escape backslashes as `\\` when needed. You can also
provide `DB_PASSWORD` as an environment variable, which overrides the file value.
The `.env` file is ignored by Git; `.env.example` contains no credentials and can
be committed. When launching from an IDE, set the working directory to the project root.

Flyway manages schema changes from `src/main/resources/db/migration` (for example,
`V1__create_orders.sql`). Hibernate validates the resulting schema at startup;
it does not create or update tables. Add migrations alongside your entities.
