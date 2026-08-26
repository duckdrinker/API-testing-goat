# API6:2023 — Unrestricted Access to Sensitive Business Flows

**Xygeni detector:** *(none)*
**Reference:** epic "Potential Improvements" section — explicitly out of scope for the current 12 detectors

## Why this fixture produces no finding

`TicketPurchaseController.purchase()` is deliberately built to be "clean" against every one of
Xygeni's 12 API Security detectors: authentication is required, a role check (`hasRole('CUSTOMER')`)
is present, and a rate limiter (bucket4j) is wired on the endpoint. None of `unauthenticated_endpoint`,
BFLA, or `rate_limit_absence` should fire here.

The API6:2023 risk this endpoint illustrates lives at the **business-logic level**: a legitimate,
authenticated, rate-limited customer can still automate ticket purchases up to the rate limit to
scalp a limited-availability event, because there is no purchase-velocity anomaly detection,
CAPTCHA, or per-event/per-user purchase cap. This is not a static code-shape defect the way
BOLA/BFLA/CORS/JWT misconfigurations are, so no Xygeni detector maps to it today.

## Expected scan result

```
xygeni-cli apisecurity --dir .
```

**Zero findings** on this endpoint. That is the expected, documented result — not a fixture bug.
