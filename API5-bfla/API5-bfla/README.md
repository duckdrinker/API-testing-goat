# API5:2023 — Broken Function Level Authorization

**Xygeni detector:** `broken_function_level_authorization` (BFLA)
**Reference test cases:** ASEC-073 (Phase A, model-level candidate), ASEC-074 (Phase B, Java source confirmation)

## What this fixture demonstrates

`POST /api/admin/users/{id}/lock` sits under `/admin/` and its operation name (`lock`) matches
the destructive/administrative heuristic, but the only security requirement is generic
authentication (`@PreAuthorize("isAuthenticated()")`) — there is no role-bearing requirement such
as `@PreAuthorize("hasRole('ADMIN')")` or `@Secured("ROLE_ADMIN")`.

## Expected scan result

```
xygeni-cli apisecurity --dir . --detectors broken_function_level_authorization
```

One BFLA candidate on `POST /api/admin/users/{id}/lock`:
- Phase A fires on the `/admin/` path + generic-only security scheme.
- Phase B confirms because no recognised role-check pattern is present in the handler.

## Negative counterpart (ASEC-074, "check present" branch)

`SafeAdminController.unlockUser()` (`POST /api/admin/users/{id}/unlock`) has the same shape but
declares `@PreAuthorize("hasRole('ADMIN')")` — Phase B should recognise this role-check pattern
and clear the candidate instead of confirming it.

```
xygeni-cli apisecurity --dir . --detectors broken_function_level_authorization
```
`POST /api/admin/users/{id}/unlock` should not be confirmed as a BFLA finding (cleared by Phase B).
