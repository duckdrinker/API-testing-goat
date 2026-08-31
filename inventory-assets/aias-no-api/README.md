# aias-no-api

Dedicated fixture for `test-plan-apisec-inventory-assets.html` (prefix `AIAS-*`) in
`xygeni/DepsDoctor-Test` (`TestCases/10.ApiSec/`) — the negative-case counterpart to
`aias-api-basic` for the same TP. Do not reuse from any other test plan.

Plain Maven/Java library with one real dependency (`commons-lang3`) and no REST/HTTP framework
code at all — no controllers, no route annotations, nothing any `ApiDiscoveryEngine` framework
detector would recognize.

| Case(s) | What it uses this fixture for |
|---|---|
| AIAS-001 | Confirms the detector produces zero `api_*` assets even when off (baseline). |
| AIAS-004 | With `--api-inventory` on, still zero `api_*` assets — no API framework code present. |

Command: `xygeni inventory --api-inventory --dir aias-no-api` (local — no `--upload`).

Confirmed working (2026-08-31): both cases PASS against this fixture with a real xygeni build
(v6.17.0).
