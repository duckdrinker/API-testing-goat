# Diagnostic fixture — single supported framework, manifest hit

**Xygeni component:** Framework autodiscovery (`FrameworkProbe` / `DependencyFileProbe`)

## Why this fixture exists

`DependencyFileProbe` scans manifest files (`requirements.txt`, `pyproject.toml`, `package.json`,
etc.) for known framework dependencies and emits evidence
`{frameworkId, confidence, source, supported}` per hit. A manifest hit on a known, supported
framework is the simplest possible case: `requirements.txt` here declares `fastapi` and nothing
else — no flask, no django, no djangorestframework, no JS/Java/Go/PHP/Rust manifest of any kind.

This isolates the base case of the confidence pipeline: exactly one piece of evidence, at the
manifest-hit confidence level (`0.95`), for exactly one framework, with `supported=true`. Per
`FrameworkProbeRunner`, that evidence aggregates (trivially, since there's only one candidate) to
a per-framework maximum confidence of `0.95`, which clears the default `confidenceThreshold`
(`0.5`).

Because at least one `supported=true` piece of evidence exists, `ApiDiscoveryEngine` does **not**
fall back to "load all detectors" — it filters source-code-based detectors down to the ones
implied by the detected framework set (descriptor-based detectors such as OpenAPI are always
loaded regardless, per the engine's own rule, but there is no OpenAPI/Swagger file in this
fixture to exercise that). The single code file, `fastapi_app.py`, defines one minimal endpoint,
`GET /api/ping`, using `@app.get(...)` — nothing from any other framework. The fixture is
deliberately left with no other framework's code in the tree (even though the fallback-vs-filter
distinction really only bites when there's something else around to filter out), to keep this
case clean and isolated; see `autodiscovery-mixed-signals` for the multi-framework case and
`autodiscovery-unsupported-only` for what happens when the only evidence is `supported=false`.

## Expected scan result

```
xygeni apisecurity --dir . --discovery-only -f json
```

`statistics.detectedFrameworks[]` should contain exactly one entry:
`{frameworkId: "fastapi", confidence: 0.95, supported: true}` (or the runner's aggregated
equivalent — the manifest-hit confidence, since there is only one signal to aggregate). As a
consequence, only the `fastapi` source-code detector plus the always-on descriptor-based
(OpenAPI) detector should be loaded — not the full detector catalog. `GET /api/ping` should be
discovered as a FastAPI endpoint.

See test-plan-apisec-framework-discovery.html §8 (Autodiscovery).
