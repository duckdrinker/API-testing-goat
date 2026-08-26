# Diagnostic fixture — confidence sits between the default and a custom threshold

**Xygeni component:** Framework autodiscovery (`FrameworkProbe` / `DependencyFileProbe` / `AutodiscoveryConfig`)

## Why this fixture exists

`autodiscovery-mixed-signals` (sibling fixture) proves two supported frameworks don't suppress
each other, but it cannot be used to check whether `autodiscovery.confidence_threshold` is
actually honored: both of its frameworks (`flask`, `koa`) hit the manifest-hit confidence level
(`0.95`), which clears both the default threshold (`0.5`) and the commonly-suggested custom value
(`0.9`) either way — the two configurations are indistinguishable on that fixture.

This fixture is built specifically to sit *between* those two thresholds:

- `requirements.txt` declares `fastapi` — an ordinary manifest hit, confidence `0.95`. This one
  clears any threshold up to `0.95` and guarantees the engine sees at least one piece of
  `supported=true` evidence that passes, regardless of what `confidence_threshold` is set to — so
  the "no evidence passed the threshold -> load all detectors" fallback never gets a chance to
  mask the result (see `autodiscovery-unsupported-only`/`autodiscovery-no-manifest` for that
  fallback in isolation).
- `go.mod` declares **no dependencies at all** (no `gin`, `echo`, `fiber`, `chi`, `gorilla/mux`, or
  any other recognized Go router library) — just the module/go-version header. Per
  `DependencyFileProbe`, a bare `go.mod` with no recognized router import is expected to produce
  the Go-language base confidence (`~0.85`, "a Go project exists here"), which is *lower* than a
  manifest-hit dependency match (`0.95`) but still above the default threshold (`0.5`) — landing
  it squarely between `0.5` and `0.9`.

Each side has one minimal endpoint: `GET /api/ping-fastapi` (FastAPI) and `GET /api/ping-go`
(plain `net/http`, no router library, so the Go detector's own discovery isn't confused by a
router it doesn't specifically expect).

## What this fixture can distinguish that `autodiscovery-mixed-signals` cannot

Run the scan twice against this same directory, changing only the local
`xygeni.apisecurity.yml` between runs:

1. **Default config** (no `autodiscovery:` section, or `autodiscovery.enabled: true` with no
   `confidence_threshold` override — effective threshold `0.5`):
   - `fastapi` (`0.95`) and `go` (`~0.85`) both clear `0.5`.
   - Both `GET /api/ping-fastapi` and `GET /api/ping-go` should be discovered.
2. **Custom config**, placed at the scan root:
   ```yaml
   autodiscovery:
     confidence_threshold: 0.9
   ```
   (snake_case, matching how the product module's own `README.md` documents the key)
   - If the engine actually honors this key: `fastapi` (`0.95`) still clears `0.9`, but `go`
     (`~0.85`) does not — and because `fastapi` already provides a passing, `supported=true`
     result, the "no evidence passed -> load all" fallback does **not** kick in for the whole
     scan. Net effect: `GET /api/ping-go` should **disappear** from the discovery output while
     `GET /api/ping-fastapi` remains.
   - If the engine silently ignores this key (the founded-but-unconfirmed suspicion described in
     `autodiscovery-mixed-signals`'s "Config diagnostic" section — a SnakeYAML loader with
     `skipMissingProperties=true` that doesn't bind snake_case `confidence_threshold` to the
     camelCase `confidenceThreshold` field): the effective threshold silently stays `0.5`, and
     `GET /api/ping-go` should still appear, **identically to run 1**.

Identical results between the two runs is the signal that the YAML key is being ignored; a
disappearing `GET /api/ping-go` in run 2 is the signal that it is being honored correctly.

## Expected scan result

```
xygeni apisecurity --dir . --discovery-only -f json
```

Run 1 (default config): `statistics.detectedFrameworks[]` contains `fastapi` (~0.95) and `go`
(~0.85); both `GET /api/ping-fastapi` and `GET /api/ping-go` appear.

Run 2 (`confidence_threshold: 0.9`): compare against run 1 as described above.

See test-plan-apisec-framework-discovery.html §8 (Autodiscovery).
