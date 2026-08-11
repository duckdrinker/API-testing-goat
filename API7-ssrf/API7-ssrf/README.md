# API7:2023 — Server-Side Request Forgery

**Xygeni detector:** `ssrf`
**Reference test cases:** ASEC-105 (required URL-shaped parameter on a server-side-fetch endpoint), ASEC-106 (same shape, optional parameter)

## What this fixture demonstrates

`POST /api/fetch` accepts a required parameter named `url` and fetches it server-side via
`RestTemplate.getForObject(url, ...)`, returning the response to the caller. The parameter name
matches the SSRF detector's URL-shaped heuristic and the endpoint path (`/fetch`) matches the
fetch-kind operation heuristic.

`GET /api/echo` (ASEC-106) also has a `url` parameter, but the handler just returns it directly —
no HTTP-client/fetch call is made with it. Per the actual detector source
(`SsrfDetector.java`: `Severity sev = pb.fetchFound ? Severity.high : Severity.low;`), severity
depends solely on whether Phase B finds a fetch call, not on whether the parameter is required or
optional (an earlier version of this fixture incorrectly tested an optional-parameter variant,
which turned out to be indistinguishable from the required case per the real implementation).

## Expected scan result

```
xygeni-cli apisecurity --dir . --detectors ssrf
```

One `ssrf` finding on `POST /api/fetch`, High severity (CWE-918). `GET /api/echo` should produce
a Low-severity finding (or none, depending on the detector's confidence threshold at
`fetchFound = false`).
