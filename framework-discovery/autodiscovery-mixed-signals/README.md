# Diagnostic fixture — two supported frameworks, two languages, same tree

**Xygeni component:** Framework autodiscovery (`FrameworkProbe` / `DependencyFileProbe`)

## Why this fixture exists

`DependencyFileProbe` runs its manifest scan per file, independently of language. This fixture
declares **two** manifests at the project root simultaneously: `requirements.txt` with `flask`
(Python) and `package.json` with `koa` + `@koa/router` (JS). Each is an ordinary manifest hit, so
each should produce its own evidence at the manifest-hit confidence level (`0.95`), for two
different `frameworkId`s.

`FrameworkProbeRunner` aggregates evidence *per framework* by maximum confidence — there is no
cross-framework conflict resolution, and nothing in the documented mechanism suggests two
frameworks from different languages/ecosystems compete or suppress each other. Both should clear
the default `confidenceThreshold` (`0.5`) independently and both should end up in the loaded
detector set (source-code detectors for `flask` and `koa`, plus the always-on descriptor-based
detectors).

Each framework has one minimal endpoint: `GET /api/ping-flask` (Flask, `app.route(...)`) and
`GET /api/ping-koa` (Koa, `router.get(...)` via `@koa/router`), so the discovery result can be
checked endpoint-by-endpoint as well as at the `detectedFrameworks[]` summary level.

## Expected scan result

```
xygeni apisecurity --dir . --discovery-only -f json
```

`statistics.detectedFrameworks[]` should contain **two** entries:
`{frameworkId: "flask", confidence: 0.95, supported: true}` and
`{frameworkId: "koa", confidence: 0.95, supported: true}`. Both the `flask` and `koa`
source-code detectors should be loaded (not filtered out by one another), and both
`GET /api/ping-flask` and `GET /api/ping-koa` should be discovered.

See test-plan-apisec-framework-discovery.html §8 (Autodiscovery).

## Config diagnostic — do not assume

This same fixture is also intended to be re-run manually with a custom `xygeni.apisecurity.yml`
placed at the scan root, declaring:

```yaml
autodiscovery:
  confidence_threshold: 0.9
```

(snake_case, matching how the product module's own `README.md` documents the key.) The purpose is
**not** covered by this fixture alone: it's to check whether the real engine actually raises its
effective threshold to `0.9`, or silently keeps the default `0.5`.

There is a founded-but-unconfirmed suspicion that the real config loader deserializes this file
with SnakeYAML configured with `skipMissingProperties=true` (or equivalent lenient behavior), and
that the loader's target field is *not* named/aliased to accept `confidence_threshold` in
snake_case — meaning the key would be silently ignored, `confidenceThreshold` would stay at its
compiled-in default (`0.5`), and this fixture's flask/koa confidences of `0.95` would clear the
threshold either way, masking the bug.

Since `0.95 > 0.9 > 0.5`, this fixture's manifest hits alone **cannot distinguish** "config was
respected, still passes" from "config was ignored, still passes" — both configurations produce
the same discoverable result on this fixture. Confirming which of the two is actually happening
requires whoever runs the scan to try **both** configurations (default vs. the custom YAML above)
against a case where the confidence sits *between* `0.5` and `0.9` (this fixture does not
currently provide one), or to inspect the engine's resolved config/logs directly. This fixture
only sets up the two-framework manifest scenario the config would apply to — it does not, by
itself, prove or disprove the SnakeYAML hypothesis.
