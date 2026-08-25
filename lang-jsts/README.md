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
- `GET /api/role-products/:id` — `requireRole('user')` is a bare role check, not an ownership
  check → xygeni-product-backlog#1551, BOLA should still be confirmed here.

## Expected scan result

```
xygeni-cli apisecurity --dir . --detectors excessive_data_exposure,pii_leak_in_response,broken_object_level_authorization,broken_function_level_authorization,cors_misconfiguration,jwt_misconfiguration,mass_assignment
```

## Framework / API Discovery additions

**Xygeni component:** API Discovery framework detector `express`

See test-plan-apisec-framework-discovery.html.

Mount resolution and route-shape coverage added on top of the detectors above (same `app.js`,
no existing route touched):

- `app.use('/api/v2/users', require('./routes/users'))` — cross-file mount via inline `require()`.
- `const adminRoutes = require('./routes/admin'); app.use('/api/v2/admin', adminRoutes)` —
  cross-file mount by variable.
- `routes/users.js` `GET /:id` — path param synthesized from a plain `:id` segment.
- `routes/users.js` `PUT /:id` — request `DataObject` synthesized from `req.body` destructuring
  (PUT only).
- `routes/admin.js` `GET /:id?` — path param synthesized from an optional `:id?` segment.
- `routes/admin.js` `POST /:id/reset` — plain path param on a mounted router, second verb.
- `app.route('/api/products').get(listProducts).post(createProducts)` — chained route builder,
  two verbs synthesized from one path.
- `app.all('/api/webhook', webhookHandler)` — catch-all verb, handler referenced by name (a
  function declared apart, not an inline arrow).

## Expected scan result (framework discovery)

```
xygeni apisecurity --dir . --discovery-only -f json
```
