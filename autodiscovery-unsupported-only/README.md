# Diagnostic fixture — only unsupported-framework evidence

**Xygeni component:** Framework autodiscovery (`FrameworkProbe` / `DependencyFileProbe`)

## Why this fixture exists

`DependencyFileProbe` maintains `UNSUPPORTED_*` maps of dependencies that identify a real
framework Xygeni has no detector for. A hit against those maps still produces evidence —
`{frameworkId, confidence, source, supported: false}` — but `supported=false` is documented as
*pure telemetry*: it never filters which detectors get loaded, by itself.

This fixture's only manifest is `Cargo.toml`, declaring `actix-web = "4"` as its sole
dependency — a Rust framework Xygeni does not have a detector for, so the probe evidence should
come back `{frameworkId: "actix-web", supported: false}`. There is no other manifest anywhere in
the project (no `requirements.txt`, no `pyproject.toml`) — but there **is** a plain `app.py` with
a minimal Flask endpoint, deliberately with no Python manifest declaring `flask` at all.

Per `ApiDiscoveryEngine.runAutodiscovery()`: if there is no evidence with `supported=true`
anywhere (and having only `supported=false` evidence counts as "none"), the engine falls back to
loading **all** detectors — the same safe fallback as having no manifest at all. So even though
nothing declares this project as a Flask project, and even though there *is* framework evidence
(just not `supported=true` evidence), the Flask detector should still be loaded, and
`GET /api/ping` should still be discovered.

## Expected scan result

```
xygeni apisecurity --dir . --discovery-only -f json
```

`statistics.detectedFrameworks[]` should contain
`{frameworkId: "actix-web", confidence: <manifest-hit level>, supported: false}` — present as
telemetry only. Because no `supported=true` evidence exists, the full detector catalog should be
loaded (fallback), and `GET /api/ping` (Flask, undeclared) should still appear in the discovered
endpoints. If it does **not** appear, that would indicate `supported=false` evidence is
incorrectly being treated as if it were `supported=true` for filtering purposes.

See test-plan-apisec-framework-discovery.html §8 (Autodiscovery).
