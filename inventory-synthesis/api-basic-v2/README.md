# api-basic-v2

Same shape as `fixtures/api-basic` (minimal Spring Boot REST service, real `spring-boot-starter-web` dependency)
but with **different endpoints** — `GET /api/invoices`, `GET /api/invoices/{id}`, `POST /api/invoices` instead of
`/api/orders`. Used by `test-plan-apisec-inventory-synthesis.html` wherever a case needs to prove the endpoint set
changed from what was already stored: APIN-008 (prior real inventory already has APIs — ingest different
endpoints and confirm nothing is overwritten) and APIN-010 (prior synthesized inventory — ingest updated endpoints
and confirm a new synthesized analysis is created).

Command: `xygeni apisecurity --dir fixtures/api-basic-v2 --upload`

Committed as a plain (unzipped) folder, same as `fixtures/api-basic` and `fixtures/inventory-no-api`.
