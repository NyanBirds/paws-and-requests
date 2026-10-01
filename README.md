# Paws and Requests

Paws and Requests is an animal adoption platform, made for simplifying communication between animal shelters and potential homes. Shelters publish animals that are
available for adoption, visitors browse the listings and send an adoption request, and shelter staff manage
their animals, posts and incoming requests.

Live URL: The backend is published to Azure App Service
(`backend-nyanbirds`) and the frontend is built and served separately.

- Backend: [backend-nyanbirds](https://backend-nyanbirds-geguesg4chhxcebr.swedencentral-01.azurewebsites.net)
- Frontend: [frontend-nyanbirds](https://jolly-mud-084d20903.3.azurestaticapps.net/)

## Team

| Member | GitHub |
| --- | --- |
| Sara Boudzakhet | [@BlueSara](https://github.com/BlueSara) |
| Lisbeth Yang | [@lisbethxy](https://github.com/lisbethxy) |
| Vegard Borvik | [@vvetasborvik](https://github.com/vvetasborvik) |

## Domain mapping

| Placeholder | Domain term | What it represents |
| --- | --- | --- |
| `[USER]` | `User` | A registered person. Either a regular adopter (`USER`), a shelter account (`SHELTERUSER`), or an `ADMIN` |
| `[PRIMARY]` | `Post` | A shelter's adoption listing for one `Animal`, with title, description and photos |
| `[CHILD]` |`Picture`| A picture linked to either a post, a shelter or a user profile picture |
| `[TAG]` | `Species` and `Gender` on `Animal` | Fixed enums used to filter posts |
| `[INTERACTION]` | `AdoptionForm` | An adoption request sent by a user for a specific post |

## Tech stack

| Layer | Technology |
| --- | --- |
| Backend | Java 21, Spring Boot, Spring Web, Spring Data JPA, Spring Security, MapStruct, Lombok, Maven |
| Frontend | React 19, Vite, React Router |
| Database | PostgreSQL (H2 during early development) |
| Build/CI | Maven, Docker, Docker Compose, GitHub Actions |

## Running locally

One command starts the whole stack:

```bash
docker compose up --build
```

- Backend: http://localhost:8080
- Frontend (Vite dev server): http://localhost:5173

The backend reads its configuration from the environment, so an `.env` file with the
JWT secret and the PostgreSQL connection details must be present before starting. `compose.yaml` injects them
into the container.

The necessary environment variables read from the `.env` file:
- `JWT_SECRET`
- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`

Run the halves without Docker:

```bash
mvn spring-boot:run                         # backend on :8080
cd frontend && npm install && npm run dev   # frontend on :5173
```

## Tests

```bash
mvn test              # JUnit 5 + Mockito tests (service, mapper, controller, security, model)
mvn checkstyle:check  # style gate, also run in CI
```

`scripts/pre-commit` runs Checkstyle before each commit. The tests are plain unit tests with mocked
repositories, so no database is needed. CI (`.github/workflows/ci.yml`) runs Checkstyle and the unit tests on
every push to `main`/`dev` and on every pull request to `main`.

## Architecture

```
frontend (React/Vite, :5173)
        │  fetch + JWT bearer token, CORS-restricted to the dev server origin
        ▼
backend (Spring Boot REST API, :8080)
        │  Spring Data JPA / Hibernate, ddl-auto=validate
        ▼
PostgreSQL
```

**Backend** — `com.codecool.pawsandrequests`, layered as `controller` → `service` → `repository`, with
`dto` + `mapper` (MapStruct) between the web and persistence layers, and `model` for the JPA entities.
Stateless JWT auth: `JwtAuthenticationFilter` runs before `UsernamePasswordAuthenticationFilter`,
`SecurityConfig` grants public read access to posts, shelters, pictures and login/registration, and requires
the `SHELTERUSER` role to create posts. Ownership rules (a shelter may only edit its own posts, animals and
requests) are enforced in the service layer and mirrored in the frontend by comparing the logged-in user's
`orgNr` with the resource's.

**Frontend** — a Vite SPA. `src/api/client.js` wraps `fetch` with the base URL, the bearer token and error
handling; `src/services/*` holds one module per resource; `src/pages/*` holds the routed screens
(home, posts, animal profile, adoption form, login/register, account, my posts/animals/requests, shelters).

**Database** — PostgreSQL, with the URL, credentials and schema owned by the environment. 

### API overview

| Method | Path | Access |
| --- | --- | --- |
| `POST` | `/api/auth/registration`, `/api/auth/login` | public |
| `GET` | `/posts`, `/posts/{postId}`, `/shelters`, `/shelters/{orgNr}`, `/pictures/**` | public |
| `POST` | `/posts` | `SHELTERUSER` |
| `PUT`/`PATCH`/`DELETE` | `/posts/**`, `/animals/**`, `/shelters/{orgNr}`, `/users/{id}` | authenticated, owner-scoped |
| `GET` | `/animals`, `/animals/{id}` | authenticated |
| `POST`/`GET` | `/posts/{postId}/adoption` | authenticated |

### Deployment

The backend is containerised with a multi-stage `Dockerfile` and published to
Azure App Service by `.github/workflows/main_backend-nyanbirds.yml` on every push to `main`. The frontend runs
as its own container from `frontend/Dockerfile` (`npm run dev --host` with the source mounted, which
`compose.yaml` wires up for local work).

## Structure

```
├── config/checkstyle/       # Checkstyle rules used by the build
├── scripts/                 # pre-commit hook
├── src/main/java/com/codecool/pawsandrequests/
│   ├── config/              # security, password encoding, JWT properties
│   ├── controller/          # REST endpoints + global exception handler
│   ├── dto/                 # request/response records
│   ├── mapper/              # MapStruct mappers
│   ├── model/               # JPA entities and enums
│   ├── repository/          # Spring Data repositories
│   ├── security/            # JWT filter
│   └── service/             # business logic
├── src/test/java/           # unit tests
├── frontend/src/            # React SPA
├── compose.yaml             # backend + frontend
└── Dockerfile
```

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

`<type>/<short-case-description>`, branched from `dev` and merged into `dev` via pull request.

| Type | Used for |
| --- | --- | 
| `feat` | new functionality | 
| `fix` | bug fixes | 
| `refactor` | restructuring without behaviour change | 
| `chore` | build, tooling, config, docs | 
| `test` | test-only changes | 
