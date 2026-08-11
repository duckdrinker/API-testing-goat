# API10:2023 — Unsafe Consumption of APIs

**Xygeni detector:** *(none)*
**Reference:** no detector in the 12 shipped covers this category (distinct from `ssrf`, see note below)

## Why this fixture produces no finding

`WeatherProxyController.getWeather()` is authenticated and calls a **fixed, hardcoded** third-party
URL, forwarding the `city` parameter as a query value — `city` is not a URL-shaped parameter name,
so this does **not** match the `ssrf` detector's heuristic (which looks for parameters named
`url`/`callback_url`/`webhook_url`/etc. that are themselves fetched). That distinction is intentional:
API7 (SSRF) is about the caller controlling *which* server-side request is made; API10 is about
blindly trusting *what comes back* from a third-party API this service already knows it's calling
(no response schema validation, no redirect limits, no TLS pinning).

None of this is a source-code shape pattern any of Xygeni's 12 API Security detectors look for —
they analyse this project's own exposed endpoints, not how it consumes an external one.

## Expected scan result

```
xygeni-cli apisecurity --dir .
```

**Zero findings** on this endpoint. That is the expected, documented result — not a fixture bug.
