# FlawCorrelator — composite/severity-adjust rules

**Xygeni component:** `FlawCorrelator` (post-pass over per-detector findings)
**Reference test cases:** ASEC-038, ASEC-039, ASEC-040

This fixture is not tied to a single OWASP category — each endpoint is purpose-built to trigger
**two underlying detectors on the same endpoint**, so the correlator's default rules fire.

## What each endpoint demonstrates

| Endpoint | Underlying detectors | Correlator rule | Expected outcome |
|---|---|---|---|
| `GET /api/patients/{id}` | `unauthenticated_endpoint` + `pii_leak_in_response` (unreferenced `diagnosis` field) | `pii_leak_in_unauthenticated_endpoint` | New composite finding, severity **critical** (originals kept) |
| `GET /api/internal/support-ticket` | `sensitive_param_unauthenticated` (`email` param) + `zombie_endpoint` (absent from `openapi.yaml`) | `zombie_endpoint_with_sensitive_param` | New composite finding, severity **high** (originals kept) |
| `GET /api/orders/{id}` | `unauthenticated_endpoint` + `excessive_data_exposure` (unreferenced `creditCardNumber` field) | `excessive_exposure_on_unauthenticated_endpoint` | Existing `excessive_data_exposure` finding's severity bumped to **high** (no new finding row) |

`GET /api/patients/{id}` and `GET /api/orders/{id}` are declared in the companion `openapi.yaml`
with **no** security requirement (confirmed empirically — an earlier version of this fixture
declared `security: bearerAuth` on the spec side, assuming the engine would defer to the
source-code model when both are combined; it does not: the engine merges spec-declared security
with the source model, so a spec-side `security: bearerAuth` marks the endpoint authenticated
overall even though the source code has no `@PreAuthorize` at all). Leaving both sources without a
security declaration is what actually lets `PatientController`/`OrderController` surface as
`unauthenticated_endpoint` candidates, which rules 1 and 3 need.
`GET /api/internal/support-ticket` is deliberately **not** declared at all, so it is the one that
produces the `zombie_endpoint` half of rule 2.

## Expected scan result

```
xygeni-cli apisecurity --dir . --spec openapi.yaml
```

3 composite/severity-adjusted outcomes as listed above, in addition to the underlying
per-detector findings (kept, not replaced, for rules 1 and 2).
