# inventory-no-api

Fixture for **APIN-009** (`test-plan-apisec-inventory-synthesis.html`): a project with real, scannable
dependencies (so `xygeni inventory` reports packages — a non-`api` asset kind) but with **no HTTP API framework
code at all** (no Spring MVC, JAX-RS, Express, FastAPI, etc.). Used to build the "real inventory, no APIs"
precondition: `xygeni inventory`'s own API discovery must report zero `api_*` assets here, while packages/repo
assets are still written normally.

It's a small offline batch tool (`ReportGenerator`): reads a JSON file of sales records from disk, converts it to
CSV, and writes it back to disk. No servlet container, no listener, no route of any kind — just a `main()` method.

Real Maven dependencies declared in `pom.xml` (for the SCA/package side of the inventory scan):
`commons-lang3`, `jackson-databind`, `junit-jupiter` (test scope).

Command: `xygeni inventory --dir fixtures/inventory-no-api --upload`

Note: this fixture is committed as a plain (unzipped) folder, unlike the other `10.ApiSec/fixtures/*.zip` fixtures,
which have moved to an external location outside the repo as of 2026-08-25. Added directly in-repo for now at the
user's request — revisit its location if/when the rest of the ApiSec fixtures come back under version control.
