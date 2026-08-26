# API8:2023 — Security Misconfiguration

**Xygeni detector:** `cors_misconfiguration`
**Reference test cases:** ASEC-078 (Java/Spring wildcard CORS), ASEC-084 (severity: wildcard-alone vs wildcard+credentials)

## What this fixture demonstrates

- `PublicStatusController` (`GET /api/public/status`) uses `@CrossOrigin` with no explicit
  `origins` (wildcard default), no credentials → `cors_misconfiguration`, severity Low.
- `AccountSummaryController` (`GET /api/public/account-summary`) uses
  `@CrossOrigin(origins = "*", allowCredentials = "true")` — the dangerous wildcard + credentials
  combination → `cors_misconfiguration`, severity High.

The two endpoints live in **separate controller files** on purpose. `cors_misconfiguration`
(`CorsMisconfigurationDetector.java`) analyses each handler file as a whole and reports once per
module/controller, not once per endpoint — two different CORS declarations in the same file would
collapse into a single combined finding (severity = worst of the two) instead of two distinct
severities. An earlier version of this fixture had both endpoints in one `PublicDataController`
and could never demonstrate the Low/High distinction for exactly this reason.

## Expected scan result

```
xygeni-cli apisecurity --dir . --detectors cors_misconfiguration
```

Two findings: one Low on `/api/public/status`, one High on `/api/public/account-summary`.
