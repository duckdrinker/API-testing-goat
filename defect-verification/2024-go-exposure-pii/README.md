# Fixture #2024 (Go) - excessive_data_exposure / pii_leak_in_response on Go

Backs ticket `xygeni-product-backlog#2024` item 1 (Go half). The dev states `excessive_data_exposure_go` and
`pii_leak_in_response_go` exist; the ticket reported zero Go findings. Validated with scanner **6.20.0**
(detector set `e565dece752b`, 2026-10-05).

Uses the archetype shape from the ticket: authenticated endpoint, id as **path parameter**, **declared** response type.

```
xygeni-cli apisecurity --dir . --detectors excessive_data_exposure,pii_leak_in_response
```

## Cases and actual results (6.20.0)

| Case | Route | Expected | Actual | Match |
|---|---|---|---|---|
| A | GET /api/users/{id}/profile (`UserProfileResponse`: Ssn, Email, Phone, PasswordHash) | excessive_data_exposure + pii_leak_in_response | `excessive_data_exposure_go` (high) + `pii_leak_in_response_go` (high) | yes |
| B | GET /api/users/{id}/summary (`UserSummaryResponse`: Id, Name) | no exposure/PII finding | no exposure/PII finding | yes |
| Auth | both routes behind `api.Use(authMiddleware)` | authenticated | `go-route-middleware` / `group-auth-middleware` | yes |

## Notes

- Confirms the dev: the Go variants exist and fire with the ticket's archetype shape. The `lang-go` README claim that
  exposure/PII do not apply to Go is outdated.
- `broken_object_level_authorization` (high) also fires on **both** A and B (no object-ownership check, id path param).
  Not planned in this fixture; it looks legitimate. It also contradicts the ticket's table, where Go BOLA is 44, not a gap.
- `rate_limit_absence` (low) once per service. Irrelevant to #2024.
- Discovery reports the routes as `/users/{id}/...`, without the `/api` prefix from `PathPrefix("/api").Subrouter()`.
  Unrelated to #2024; not analysed further.
