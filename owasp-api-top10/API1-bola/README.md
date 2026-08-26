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

## Repo-scoping pattern, positive and negative (xygeni-product-backlog#1386)

`RepoScopingPositiveController.getTenantProduct()` (`GET /api/tenant-products/{id}`) has the same
BOLA shape but calls `repository.selectForCurrentTenant(id)` — a repository method whose name only
counts as an ownership-check pattern once `selectForCurrentTenant` is added to the detector's
`repo_scoping` configuration. Phase B should clear this candidate only when that custom config is
present.

`RepoScopingNegativeController.getAgentProduct()` (`GET /api/agent-products/{id}`) calls
`repository.findByUserAgent(id)` instead — a name that textually matches the built-in
`By...User...` shape, but the walker's token-boundary check must still reject it as a false
ownership signal (a User-Agent header lookup scopes nothing). Phase B must still confirm this
candidate even with a custom `repo_scoping` pattern configured elsewhere — a custom pattern must
not weaken the built-in strict check.

```
xygeni-cli apisecurity --dir . --detectors broken_object_level_authorization
```
Without the custom `repo_scoping` config: both endpoints confirmed as BOLA findings.
With `selectForCurrentTenant` added to `repo_scoping`: only `/api/tenant-products/{id}` is cleared;
`/api/agent-products/{id}` remains confirmed.

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
