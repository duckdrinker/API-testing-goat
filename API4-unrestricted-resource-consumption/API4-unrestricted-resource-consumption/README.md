# API4:2023 — Unrestricted Resource Consumption

**Xygeni detector:** `rate_limit_absence`
**Reference test case:** ASEC-099 (JS/Express — no rate-limit middleware)

## What this fixture demonstrates

`POST /api/password-reset` is authenticated (`requireAuth` middleware) and accepts user input
(`email`), but neither this route nor the rest of the app wires `express-rate-limit` or
`rate-limiter-flexible` anywhere. A single caller can trigger unlimited password-reset emails.

## Expected scan result

```
xygeni-cli apisecurity --dir . --detectors rate_limit_absence
```

One `rate_limit_absence` finding on `POST /api/password-reset`, severity High (write endpoint).

## Negative counterpart (ASEC-104)

`POST /api/feedback` has the same shape (authenticated, write, user input) but wires
`express-rate-limit` (`feedbackLimiter`) — no `rate_limit_absence` finding expected for this
endpoint.
