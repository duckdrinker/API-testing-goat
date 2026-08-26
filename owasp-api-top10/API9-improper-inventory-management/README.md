# API9:2023 — Improper Inventory Management

**Xygeni detectors:** `zombie_endpoint`, `orphan_spec`
**Reference test cases:** ASEC-049 (zombie_endpoint), ASEC-050 (orphan_spec)

## What this fixture demonstrates

- `GET /api/users` is implemented in code **and** declared in `openapi.yaml` — a clean, matched
  reference endpoint (no finding).
- `GET /api/legacy/report` is implemented in code (`ApiController.legacyReport()`) but intentionally
  **omitted** from `openapi.yaml` → `zombie_endpoint`.
- `GET /api/orphaned-endpoint` is declared in `openapi.yaml` but has **no implementation** anywhere
  in this project → `orphan_spec`.

## Expected scan result

```
xygeni-cli apisecurity --dir . --spec openapi.yaml --detectors zombie_endpoint,orphan_spec
```

Two findings: `zombie_endpoint` on `GET /api/legacy/report`, `orphan_spec` on `GET /api/orphaned-endpoint`.
`GET /api/users` should not produce a finding from either detector.

## Negative counterpart (ASEC-051, trivial normalization)

`GET /api/orders/{id}` (`ApiController.getOrder()`) is declared in `openapi.yaml` as
`/api/orders/{orderId}` — same method and path structure, only the path-parameter *name* differs.
The normalized join should still succeed, so this endpoint should **not** be reported as
`zombie_endpoint` or `orphan_spec` despite the naming difference.
