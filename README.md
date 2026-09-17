## Structure



## Naming Conventions

| Identifier Type | Case Convention | Word Type | Quick Examples |
| -------- | ------- | -------- | ------- |
| **Classes** | `PascalCase` | Noun | `UserAccount`, `CustomerOrder` |
| **Interfaces** | `PascalCase` | Adjective / Noun | `Runnable`, `Serializable` |
| **Methods** | `camelCase` | Verb | `calculateTotal()`, `getUserId()` |
| **Variables** | `camelCase` | Noun | `accountBalance`, `itemCount` |
| **Constants** | `UPPER_CASE` | Noun | `MAX_LIMIT`, `PI` |
| **Packages** | `lowercase` | Noun | `com.company.project.module` |

## Branch Naming


<!-- ============================================================
REVIEW: repo-level notes (instructor review pass, 2026-09-16)

Inline comments are marked REVIEW(azure) / REVIEW(sec) / REVIEW(api) /
REVIEW(bug) / REVIEW(noob) / REVIEW(config) / REVIEW(efficiency) /
REVIEW(good) in the source. This branch exists to be read as a diff.
Do not merge it: read it, act on what you agree with, then delete it.

The findings below have no single line to attach to.

1. SECURITY IS DENY-LAST INSTEAD OF DENY-FIRST. SecurityConfig ends with
   .anyRequest().permitAll(), so only DELETE /posts/** is protected and every
   endpoint added from now on ships unprotected unless somebody remembers to
   come back and add a matcher. GET /animals is meant to be shelter-private and
   is currently wide open, which is also why it returns 500 rather than 401 when
   called without a token. Flip it to .anyRequest().authenticated() with an
   explicit permitAll for /api/auth/**.

2. DEPLOYMENT WOULD NOT SURVIVE A RESTART. The only datasource is an in-memory
   H2, seeded from data.sql on every boot, with the H2 console switched on and
   publicly reachable. The PostgreSQL driver is already in the pom; the work is
   moving the URL to an environment variable and keeping H2 for the dev profile.

3. NO DOCKERFILE AND NO COMPOSE FILE. Nothing has ever been containerized, so
   the first docker build will happen under deadline pressure. A Spring Boot
   multi-stage build is about fifteen lines.

4. NO CORS CONFIGURATION anywhere. Invisible while the frontend is proxied by
   the Vite dev server, fatal the moment both halves are deployed to different
   hostnames.

5. NO TESTS AT ALL. src/test does not exist, so the mvn test step in CI passes
   by running nothing. Start with PostService.deletePost: the role switch in
   there is the only real business rule in the project and it has four branches.

6. NO FRONTEND IN THIS REPO, and the capstone is a fullstack project.

7. THE README IS EMPTY apart from a naming-convention table. Nobody can run this
   project from the repo alone. Minimum: the required environment variables
   (JWT_SECRET and its expected format, base64), how to start the database, how
   to run, and the endpoint list.
============================================================ -->
