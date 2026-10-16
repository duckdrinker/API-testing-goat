# Fixture #2176 - Spring-Kotlin and Ktor parse the same `.kt`

Each case folder is an independent project. Scan with `xygeni scan --run=apisec --dir <case> ...`.

| Case | Content | Expected endpoints |
|---|---|---|
| `case-a-spring-first` | Spring controller sorts first (`AOrderController`), Ktor app/routes/wrapper after | see below |
| `case-b-ktor-first` | Ktor app sorts first (`AApp`), Spring controller last (`ZOrderController`); route function and `secured` wrapper live in later files | see below |
| `control-ktor-only` | Same Ktor files, no Spring | `/health`, `/api/items` GET+POST, `/api/items/public` |
| `control-spring-only` | Only the Spring controller | `GET /orders/{id}` |

Expected in every mixed case (same result as controls, no loss from the shared parse):

| Endpoint | Auth |
|---|---|
| GET /health | none (legitimate) |
| GET /api/items | required (`secured` -> `authenticate("auth-jwt")`, declared in another file) |
| POST /api/items | required |
| GET /api/items/public | none (control: sibling of the wrapped routes) |
| GET /orders/{id} | Spring controller, reported once |

The parse count itself is not observable from the CLI (covered by `KotlinParseSharingTest` in DepsDoctor).
