# Diagnostic fixture — no manifest evidence at all

**Xygeni component:** Framework autodiscovery (`FrameworkProbe` / `DependencyFileProbe`)

## Why this fixture exists

This fixture contains a single Python source file (`app.py`) with a minimal Flask endpoint and
**no manifest file of any kind** — no `requirements.txt`, no `pyproject.toml`, no `package.json`,
no `pom.xml`, no `Cargo.toml`, nothing `DependencyFileProbe` can scan.

Per `ApiDiscoveryEngine.runAutodiscovery()`, a project with no manifest evidence at all produces
no `FrameworkProbe` evidence whatsoever — which is exactly the same terminal state as "no
evidence with `supported=true`" (see `autodiscovery-unsupported-only` for the sibling case where
there *is* evidence, just none of it `supported=true`). Both cases hit the same fallback: load
**all** detectors. `statistics.detectedFrameworks[]` should therefore come back empty (or absent),
while the Flask endpoint should still be discovered because the Flask detector is loaded
unconditionally under the fallback.

This is the cleanest possible check of the fallback rule in isolation: no manifest, no
`supported=false` noise, nothing to filter on at all — just source code and the fallback.

## Expected scan result

```
xygeni apisecurity --dir . --discovery-only -f json
```

`statistics.detectedFrameworks[]` should be empty. `GET /api/ping` (Flask) should still be
discovered, because the absence of any evidence triggers the "load all detectors" fallback, not
"load no detectors."

See test-plan-apisec-framework-discovery.html §8 (Autodiscovery).
