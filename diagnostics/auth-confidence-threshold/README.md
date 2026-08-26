# Diagnostic fixture — unauthenticated_endpoint confidence-scoring matrix

**Xygeni detector:** `unauthenticated_endpoint`
**Related to:** ASEC-043 (note), ASEC-044, ASEC-045

## Why this fixture exists

While investigating why `unauthenticated_endpoint` didn't fire on
`API2-broken-authentication`'s `AccountController.getBalance()` (a genuinely unauthenticated
endpoint with no per-method/class auth annotation — the textbook ASEC-043 case), we found the
detector's evidence carries a scoring structure, not a flat rule:

```json
"classification": {
  "verdict": "AUTH_REQUIRED",
  "anonymousScore": 0,
  "authRequiredScore": 10,
  "authRequiredReasons": [
    "response returns sensitive (PII/PCI/PHI) fields",
    "accepts sensitive input (non-credential)",
    "path matches resource convention (/admin /users /payments …)"
  ]
}
```

`AccountController.getBalance()` has none of these 3 signals (no PII in the response, no sensitive
input parameter, path doesn't match a resource convention) and gets classified `UNDETERMINED`
(suppressed, no finding) instead of `AUTH_REQUIRED`. `UserProfileController.getProfile()` in
`API3-broken-object-property-level-authorization` has all 3 signals and gets confirmed. This
fixture fills in the 6 remaining combinations of the 2×2×2 matrix to map out where the confidence
threshold actually sits.

## Endpoints

| Endpoint | Path convention (P) | Sensitive response (R) | Sensitive input (I) |
|---|---|---|---|
| `GET /api/users/summary` | ✓ | | |
| `GET /api/widgets/secret` | | ✓ | |
| `GET /api/widgets/lookup` | | | ✓ |
| `GET /api/users/secret` | ✓ | ✓ | |
| `GET /api/users/lookup` | ✓ | | ✓ |
| `GET /api/widgets/full` | | ✓ | ✓ |

No endpoint has any auth annotation, and there is no `SecurityConfig` in this project at all — the
only variable across endpoints is the P/R/I signal combination.

## Expected scan result

```
xygeni apisecurity --dir fixtures/auth-confidence-threshold -o out.json -f json
```

Inspect each endpoint's flaw (or absence of one) and, where present, its
`properties.classification.authRequiredScore` / `verdict` to determine which signal
combinations cross the confidence threshold into a reported `unauthenticated_endpoint` finding.
