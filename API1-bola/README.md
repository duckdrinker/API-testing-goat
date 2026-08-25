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

## Additional case: xygeni-product-backlog#1551 (bare role check ≠ ownership check)

`RoleOnlyProductController.getProduct()` (`GET /api/user-role-products/{id}`) has the same BOLA
shape but is guarded by `@PreAuthorize("hasRole('USER')")` — a plain, non-privileged role check,
not an ownership check.

```
xygeni-cli apisecurity --dir . --detectors broken_object_level_authorization
```
`GET /api/user-role-products/{id}` should still be confirmed as a BOLA finding — a bare role
check must not clear the candidate the way a genuine ownership check (or a privileged role like
`ADMIN`) does.

## Bonus: ASEC-102 (rate_limit_absence, Java/Spring)

`ReviewController.addReview()` (`POST /api/products/{id}/reviews`) is authenticated and accepts
user input (the review body), but there is no `@RateLimiter`, bucket4j, or Resilience4j usage
anywhere in this project.

```
xygeni-cli apisecurity --dir . --detectors rate_limit_absence
```
One `rate_limit_absence` finding on `POST /api/products/{id}/reviews`, severity High (write endpoint).
