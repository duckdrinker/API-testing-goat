# Diagnostic fixture — rate_limit_absence, isolated (no sibling rate-limited route)

**Related to:** ASEC-099 (rate_limit_absence, JS/Express)

## Why this fixture exists

`fixtures/API4-unrestricted-resource-consumption` never fires `rate_limit_absence` on
`POST /api/password-reset` (an authenticated, unprotected write endpoint — the textbook
positive case), even though the fixture is correctly built. Its `package.json` declares
`express-rate-limit` as a dependency (used only by the sibling `/api/feedback` route, for the
ASEC-104 negative case). Hypothesis: the detector may check the dependency manifest for a known
rate-limiting library name and suppress the finding project-wide if found, rather than checking
actual per-route usage.

This fixture isolates `POST /api/password-reset` alone, with **no** sibling route and **no**
rate-limiting library declared anywhere in `package.json`, to test that hypothesis.

## Expected scan result

```
xygeni apisecurity --dir fixtures/express-isolated -o out.json -f json
```

If `rate_limit_absence` fires here (unlike in the combined API4 fixture), that confirms the
dependency-manifest hypothesis. If it still doesn't fire, the root cause lies elsewhere.
