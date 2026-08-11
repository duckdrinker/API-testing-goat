# Language coverage — JS/TS (Express)

**Xygeni detectors:** `excessive_data_exposure`, `pii_leak_in_response`, `broken_object_level_authorization` (Phase B), `broken_function_level_authorization` (Phase B), `cors_misconfiguration`, `jwt_misconfiguration`, `mass_assignment`
**Reference test cases:** ASEC-058/059, ASEC-066/067, ASEC-071, ASEC-076, ASEC-079, ASEC-088, ASEC-095

Separate from `API4-unrestricted-resource-consumption/` (which stays focused on `rate_limit_absence`
only) so that fixture doesn't grow beyond its OWASP-category theme.

## What this fixture demonstrates

- `GET /api/users/profile` — `ssn` unreferenced by the request, sensitivity-tagged →
  `excessive_data_exposure` (058) + `pii_leak_in_response` (066).
- `GET /api/users/:id/summary` (negative) — only operation-relevant fields → no finding (059, 067).
- `PUT /api/users/profile` — `role` binds through to the persisted user → `mass_assignment` (095).
- `PUT /api/users/:id/safe-update` (negative) — no bindable protected field → no finding.
- `GET /api/products/:id` — BOLA Phase B, check absent (071).
- `GET /api/safe-products/:id` — `req.user.id === obj.userId` present → cleared (071, "present" branch).
- `POST /api/admin/users/:id/lock` — BFLA Phase B, check absent (076).
- `POST /api/admin/users/:id/unlock` — `requireRole('admin')` middleware present → cleared (076).
- `app.use('/api/public', cors())` with no options → `cors_misconfiguration` (079).
- `verifyToken()` — `jwt.verify(token, key, { algorithms: ['none'] })` → `jwt_misconfiguration` (088).

## Expected scan result

```
xygeni-cli apisecurity --dir . --detectors excessive_data_exposure,pii_leak_in_response,broken_object_level_authorization,broken_function_level_authorization,cors_misconfiguration,jwt_misconfiguration,mass_assignment
```
