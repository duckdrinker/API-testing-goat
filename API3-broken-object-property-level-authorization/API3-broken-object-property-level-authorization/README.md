# API3:2023 — Broken Object Property Level Authorization

**Xygeni detectors:** `sensitive_param_unauthenticated`, `excessive_data_exposure`, `pii_leak_in_response`, `mass_assignment`
**Reference test cases:** ASEC-046, ASEC-052, ASEC-060, ASEC-092

## What this fixture demonstrates

A single `UserProfileController` (no auth annotation anywhere) combines all four API3-mapped
detectors around one request/response shape:

- `GET /api/users/profile?ssn=...` — the `ssn` query parameter is sensitivity-tagged (PII) and the
  endpoint requires no authentication → `sensitive_param_unauthenticated`.
- The same endpoint's response DTO (`UserProfileResponse`) carries an `ssn` field that is never
  part of the request and is sensitivity-tagged → `excessive_data_exposure` (high confidence) and
  `pii_leak_in_response`.
- `PUT /api/users/profile` accepts a `role` field (`UserProfileUpdateRequest.role`) that binds
  straight through to the persistent `User` entity, which a client should never be able to set
  directly → `mass_assignment`.

## Expected scan result

```
xygeni-cli apisecurity --dir . --detectors sensitive_param_unauthenticated,excessive_data_exposure,pii_leak_in_response,mass_assignment
```

Four findings on the two endpoints above.

## Negative counterparts

All three require authentication now (`SecurityConfig` permits only `/api/users/profile`;
everything else defaults to `anyRequest().authenticated()`):

- **ASEC-047** (sensitive param, authenticated): `GET /api/users/secure-profile?ssn=...` — same
  `ssn` shape, but `@PreAuthorize("isAuthenticated()")` — no `sensitive_param_unauthenticated`.
- **ASEC-053 / ASEC-061** (no over-exposure / no PII leak): `GET /api/users/{id}/summary` returns
  `UserSummaryResponse` (`id`, `name` only) — no unreferenced sensitivity-tagged field, so neither
  `excessive_data_exposure` nor `pii_leak_in_response` should fire.
- **ASEC-098** (no bindable protected field): `PUT /api/users/{id}/safe-update` accepts
  `UserProfileSafeUpdateRequest` (`name`, `email` only, no `role`) — no `mass_assignment`.

```
xygeni-cli apisecurity --dir . --detectors sensitive_param_unauthenticated,excessive_data_exposure,pii_leak_in_response,mass_assignment
```
None of `secure-profile`, `{id}/summary`, or `{id}/safe-update` should appear in the findings.
