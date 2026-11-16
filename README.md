# OrderCore

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
