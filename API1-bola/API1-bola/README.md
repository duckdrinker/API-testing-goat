# API1:2023 — Broken Object Level Authorization

**Xygeni detector:** `broken_object_level_authorization` (BOLA)
**Reference test cases:** ASEC-068 (Phase A, model-level candidate), ASEC-069 (Phase B, Java/Spring source confirmation)

## What this fixture demonstrates

`ProductController` exposes `GET/PUT/DELETE /api/products/{id}`. Every method requires
authentication (`@PreAuthorize("isAuthenticated()")`) but none of them verify that the
authenticated caller actually owns the `{id}` resource being accessed — there is no
`@PreAuthorize("#id == authentication.principal.id")` (or equivalent) pattern anywhere
in the handler.

## Expected scan result

```
xygeni-cli apisecurity --dir . --detectors broken_object_level_authorization
```

Each of the three endpoints should be reported as a BOLA candidate:
- Phase A fires because the endpoints match the `(GET|PUT|PATCH|DELETE) /resource/{id}` shape,
  require authentication, and the path parameter (`id`) has no ownership-check indicator.
- Phase B confirms (raises confidence to High) because the handler body contains no recognised
  ownership-check pattern for Java/Spring.

## Negative counterpart (ASEC-069, "check present" branch)

`SafeProductController.getOwnProduct()` (`GET /api/safe-products/{id}`) has the same shape but
declares `@PreAuthorize("#id == authentication.principal.id")` — Phase B should recognise this
ownership-check pattern and clear the candidate instead of confirming it.

```
xygeni-cli apisecurity --dir . --detectors broken_object_level_authorization
```
`GET /api/safe-products/{id}` should not be confirmed as a BOLA finding (cleared by Phase B).

## Regression check: privilegedRoles default trim (xygeni-product-backlog#1400)

`ManagerScopedProductController.getProduct()` (`GET /api/manager-products/{id}`) has the same
shape as `ProductController.getProduct()` (no ownership check) but is gated by
`@PreAuthorize("hasRole('MANAGER')")` instead of `isAuthenticated()`. Before #1400, `manager` was
part of the hardcoded privileged-role list and this endpoint would have been cleared (suppressed)
by Phase B. After #1400 trimmed the shipped `privilegedRoles` default on
`broken_object_level_authorization.yml` to the unambiguous admin/root family, `manager` is no
longer privileged by default.

```
xygeni-cli apisecurity --dir . --detectors broken_object_level_authorization
```

`GET /api/manager-products/{id}` should now be reported as a BOLA finding (new true positive after
#1400) instead of being cleared.

## Bonus: ASEC-102 (rate_limit_absence, Java/Spring)

`ReviewController.addReview()` (`POST /api/products/{id}/reviews`) is authenticated and accepts
user input (the review body), but there is no `@RateLimiter`, bucket4j, or Resilience4j usage
anywhere in this project.

```
xygeni-cli apisecurity --dir . --detectors rate_limit_absence
```
One `rate_limit_absence` finding on `POST /api/products/{id}/reviews`, severity High (write endpoint).
