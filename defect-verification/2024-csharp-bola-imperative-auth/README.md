# Fixture #2024 (C#) - BOLA on C#: authentication inferred from imperative `AuthorizeAsync`

Backs ticket `xygeni-product-backlog#2024` item 3 (fix: #2018 / DepsDoctor#4450). Validated with scanner **6.20.0**
(detector set `e565dece752b`, 2026-10-05). Modelled on Orchard Core: authorization performed inside the handler
instead of via `[Authorize]`.

```
xygeni-cli apisecurity --dir . --detectors broken_object_level_authorization,unauthenticated_endpoint
```

## Cases and actual results (6.20.0)

| Case | Route | Expected | Actual | Match |
|---|---|---|---|---|
| A | GET /api/items/{id} (policy-only `AuthorizeAsync`) | authenticated, BOLA | authenticated (`IAuthorizationService.AuthorizeAsync()`), BOLA high | yes |
| B | GET /api/items/owned/{id} (object-level `AuthorizeAsync(User, item, ..)`) | authenticated, BOLA cleared | BOLA suppressed (info), `FRAMEWORK_ENFORCED:IAuthorizationService.AuthorizeAsync(user, resource, requirement)` | yes |
| C | GET /api/items/public/{id} (no auth) | only `unauthenticated_endpoint` | only `unauthenticated_endpoint` (info) | yes |
| D | GET /api/items/by-name/{name} (imperative, name-keyed) | authenticated, no BOLA | authenticated, no finding | yes |
| E | GET /api/minimal/items/{itemId} (minimal API, `.AllowAnonymous()`, handler authz) | per #2157: unauthenticated, no BOLA | **authenticated, BOLA high** | **no** |
| F | GET /api/items/attr/{id} (`[Authorize]` attribute) | authenticated, BOLA | authenticated, BOLA high | yes |

Also reported: `rate_limit_absence` (low, per service). Irrelevant to #2024.

## Notes

- A/B/D hinge on #4450 (`imperativeSecurityRequirements`); without the fix they come out `authenticated=false`.
- Phase B confirming A (policy-only check is not an ownership check) was our reading, not stated in the ticket; the scanner agrees.
- **E diverges from the dev's description in #2157.** The dev says minimal APIs with `.AllowAnonymous()` stay recorded as unauthenticated because `minimalApiSecurity` never looks at the handler. Here the endpoint is inventoried as authenticated and BOLA fires even though the handler does an object-level `AuthorizeAsync(http.User, item, "ViewItems")` (B, the controller equivalent, is suppressed). Either 6.20.0 already covers part of #2157, or this fixture does not reproduce Orchard's exact shape. If the former, the BOLA on E is a false positive. Unconfirmed with the dev. The verdict on #2024 does not depend on E.
- Every endpoint reports `objectLevel: false` in the inventory, including B (which is suppressed through the Phase B evidence, not through this flag).
- 6.20.0 prints the same version with and without the fix; the detector-set fingerprint tells them apart. This build's (`e565dece752b`) differs from both the ticket's (`14719c2cf29f`) and the dev's "before" (`f3afb7b2`).
