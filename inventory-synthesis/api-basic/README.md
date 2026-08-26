# api-basic

Minimal Spring Boot REST service — 2 endpoints (`GET /api/orders`, `GET /api/orders/{id}`) with a real
`spring-boot-starter-web` dependency. Primary "has real APIs + real dependencies" fixture for
`test-plan-apisec-inventory-synthesis.html` (prefix `APIN-*`) — used wherever a case needs `xygeni inventory` or
`xygeni apisecurity` to discover actual endpoints: APIN-001, 002, 003, 005, 007, 011, 012.

Commands:
- `xygeni inventory --dir fixtures/api-basic --upload`
- `xygeni apisecurity --dir fixtures/api-basic --upload`
- `xygeni --run=inventory,apisecurity --dir fixtures/api-basic --upload`

Committed as a plain (unzipped) folder — see the memory note `reference_apisec_fixture_zip_packaging` for why this
doesn't follow the old zip convention used by the (now externally-located) ApiSec scanner fixtures.
