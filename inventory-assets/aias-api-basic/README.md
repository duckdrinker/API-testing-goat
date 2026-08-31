# aias-api-basic

Dedicated fixture for `test-plan-apisec-inventory-assets.html` (prefix `AIAS-*`) in
`xygeni/DepsDoctor-Test` (`TestCases/10.ApiSec/`) — the TP for the formal
`api_service`/`api_module`/`api_endpoint` inventory asset model and `issuesAssetMapping` finding
links introduced by `xygeni-product-backlog#1057`. Do not reuse from any other test plan.

Minimal Spring MVC project, source-only (no OpenAPI spec), with two controllers standing in for
two different modules of a single service:

- `OrdersController` (`com.xygeni.aias.basic.orders`) — `GET /api/orders`, `POST /api/orders`,
  `GET /health`. No auth annotations, and the response leaks a raw PII-looking field
  (`creditCardNumber`) on purpose — this is the fixture's one deliberate finding.
- `InvoicesController` (`com.xygeni.aias.basic.invoices`) — `GET /api/invoices`, `GET /health`.
  No findings expected here; exists to give the fixture a second module.

Both controllers map `GET /health` as a bare top-level path (no class-level `@RequestMapping`
prefix), so the two `/health` endpoints collide exactly on `METHOD path` while living in different
modules.

| Case(s) | What it uses this fixture for |
|---|---|
| AIAS-001/002 | Detector on/off — presence/absence of `api_*` assets. |
| AIAS-003 | Full asset generation: 1 service, 2 modules, 5 endpoints. |
| AIAS-005/006 | Id format and stability across repeated scans. |
| AIAS-007/008 | `belongs_to` chain and source-location link. |
| AIAS-009 | Parity between `apisecurity` and `inventory --api-inventory` discovery. |
| AIAS-010/011/012 | `issuesAssetMapping` — the `OrdersController` PII leak is the finding to map. |
| AIAS-013 | Source-only collapse to a single default service (named after the enclosing repo), despite two differently-packaged controllers. |
| AIAS-014 | Endpoint identity scoped to module — both `/health` endpoints must survive as distinct `api_endpoint` assets. |
| AIAS-015 | `--discovery-only` still renders the embedded `services[]` tree. |

Commands (all local — no `--upload`, nothing reaches the server; see `-f json -o <path>` for a
JSON report):
- `xygeni inventory --dir aias-api-basic` (no flag — baseline, no `api_*` assets)
- `xygeni inventory --api-inventory --dir aias-api-basic`
- `xygeni apisecurity --dir aias-api-basic`
- `xygeni apisecurity --discovery-only --dir aias-api-basic`

Not a buildable/runnable project (no `Application` class, no test harness) — only source parsing
is exercised by these commands, so this is intentionally minimal.

Confirmed working end to end (2026-08-31): all 15 `AIAS-*` cases PASS against this fixture with a
real xygeni build (v6.17.0) — see the Execution Results in `DepsDoctor-Test`.
