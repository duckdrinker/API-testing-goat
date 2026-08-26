# API2:2023 — Broken Authentication

**Xygeni detectors:** `unauthenticated_endpoint`, `jwt_misconfiguration`
**Reference test cases:** ASEC-043 (unauthenticated_endpoint), ASEC-085 (JWT — Java "algorithm none")

## What this fixture demonstrates

- `AccountController.getBalance()` (`GET /api/accounts/balance`) has no authentication
  requirement at all, and its path does not match any public-path heuristic
  (`/health`, `/metrics`, `/actuator/**`, `/.well-known/**`) → `unauthenticated_endpoint`.
- `JwtTokenService` (`POST /api/token/verify`) builds its verifier with `Algorithm.none()`
  (`JWT.require(Algorithm.none())`), accepting unsigned tokens → `jwt_misconfiguration`,
  severity Critical. It is a `@RestController` with its own endpoint (not a plain `@Service`
  called from elsewhere), because `jwt_misconfiguration` only scans an endpoint's own handler
  file — it does not follow calls into other classes. An earlier version of this fixture had the
  vulnerable code in a `@Service` that no controller ever called, so the detector never read the
  file at all.

## Expected scan result

```
xygeni-cli apisecurity --dir . --detectors unauthenticated_endpoint,jwt_misconfiguration
```

Two findings: one `unauthenticated_endpoint` on `GET /api/accounts/balance`, one
`jwt_misconfiguration` (Critical) pointing at `JwtTokenService.java` / `POST /api/token/verify`.

## Negative counterparts

- **ASEC-044** (public-path heuristic): `HealthController.health()` (`GET /health`) also has no
  authentication, but its path matches the recognised public-path heuristic — no
  `unauthenticated_endpoint` finding expected.
- **ASEC-091** (JWT, properly configured): `SafeJwtTokenService` (`POST /api/token/verify-safe`)
  pins the algorithm (`Algorithm.HMAC256(...)`), sources the secret from the environment
  (`System.getenv("JWT_SECRET")`), and leaves expiration validation at its default (enabled) — no
  `jwt_misconfiguration` finding expected.

```
xygeni-cli apisecurity --dir . --detectors unauthenticated_endpoint,jwt_misconfiguration
```
Neither `GET /health` nor `SafeJwtTokenService.java` should appear in the findings.

## Sensitivity classifier — CREDENTIAL tag (ASEC-033)

`LoginController.login()` (`POST /api/login`) accepts a `password` parameter — a credential-shaped
name the `SensitivityClassifier` should tag `CREDENTIAL`, CWE-522. `/api/login` is explicitly
`permitAll` in `SecurityConfig` (a login endpoint is naturally public), so the endpoint is
genuinely unauthenticated per the model and `sensitive_param_unauthenticated` fires, surfacing the
classifier's tag directly in the flaw's evidence instead of being swallowed by the centralized
"authenticated" classification.

```
xygeni apisecurity --dir .
```
The `password` parameter on `POST /api/login` is tagged `CREDENTIAL`, and a
`sensitive_param_unauthenticated` finding is raised on `POST /api/login`.

## Known-limitation fixture (ASEC-045)

`TransactionController.listTransactions()` (`GET /api/transactions`) has **no** per-method or
per-class auth annotation — it is protected only by `SecurityConfig`'s centralized
`anyRequest().authenticated()` rule (comparable to a `WebSecurityConfig.authorizeHttpRequests()`
style declaration). Xygeni's `unauthenticated_endpoint` detector does not parse that centralized
rule, only per-method/class annotations near the handler.

```
xygeni-cli apisecurity --dir . --detectors unauthenticated_endpoint
```
`GET /api/transactions` may still be reported as an `unauthenticated_endpoint` false-positive
candidate — this is the documented, pre-existing limitation the fixture exists to demonstrate,
**not** a new defect. Verify the finding's evidence text and record the observation instead of
filing it as a fresh bug.
