# `--spec` format ingestion fixtures

**Xygeni CLI flag:** `--spec`
**Reference test cases:** ASEC-002 (OpenAPI 3.x), ASEC-003 (Swagger 2.0), ASEC-004 (Postman v2.1)

Each file here is a standalone spec document — no source code needed — declaring exactly one
endpoint with **no security scheme**, which the model-level `unauthenticated_endpoint` detector
should pick up directly from the spec-derived endpoint (no source code required).

## Expected scan results

```
xygeni-cli apisecurity --spec openapi3.yaml --detectors unauthenticated_endpoint
xygeni-cli apisecurity --spec swagger2.json --detectors unauthenticated_endpoint
xygeni-cli apisecurity --spec postman-collection.json --detectors unauthenticated_endpoint
```

- `openapi3.yaml` → one `unauthenticated_endpoint` finding on `GET /api/notifications`.
- `swagger2.json` → one `unauthenticated_endpoint` finding on `GET /api/messages`.
- `postman-collection.json` → converted internally to an OpenAPI-equivalent source; one
  `unauthenticated_endpoint` finding on `GET /api/settings`.

Each file can also be combined with any other fixture's `--dir` (e.g. `--dir ../API1-bola --spec openapi3.yaml`)
to additionally exercise multi-source discovery, but that is not required to get an issue out of them.
