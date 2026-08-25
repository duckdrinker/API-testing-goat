# Hono (TypeScript) — API Discovery framework coverage

**Xygeni component:** API Discovery framework detector `HonoFrameworkDetector`

## What this fixture demonstrates
- `const app = new Hono()` — the first `new Hono()` in the module is treated as the main app.
- `api.get('/items/:id', ...)` mounted via `app.route('/api', api)` (`GET /api/items/:id`) — sub-router mount with prefix correctly applied, path param `:id` extracted → framework discovery.
- `admin.use('*', requireAuth)` + `admin.get('/users', ...)` mounted via `app.route('/admin', admin)` (`GET /admin/users`) — auth middleware declared at the sub-router level, inherited by every route on `admin` → recognized as an auth-protected route.
- `app.all('/webhook', ...)` (`GET /webhook`, `all` reported as `GET`) — catch-all verb on the main app → framework discovery.
- `legacy.get('/orders/:id', ...)` mounted via `app.basePath('/legacy').route('/', legacy)` — exercises the known limitation below.

## Known limitation demonstrated
- **`app.basePath('/prefix')` is not resolved.** `app.route('/prefix', subApp)` (used for `api` and `admin` above) IS recognized — the detector's helper translates it into an internal "use"-style mount route so the shared mount-prefix logic applies, the same way it does for Express. `app.basePath('/prefix')`, Hono's other prefixing mechanism, is not: the third sub-router, `legacy`, is mounted via `app.basePath('/legacy').route('/', legacy)` specifically to exercise this gap. `GET /orders/:id` (declared on `legacy`) must **not** appear as `GET /legacy/orders/:id` in the discovery output — whether it is instead reported without the `/legacy` prefix, or not reported at all, is an implementation detail of the unresolved case, not something this fixture asserts. This is documented, expected behaviour of the detector, not a bug to fix here.

## Expected scan result
```
xygeni apisecurity --dir . --discovery-only -f json
```
Discovered endpoints should include `GET /api/items/:id`, `GET /admin/users` (auth-protected, inherited from the sub-router-level `admin.use('*', requireAuth)`), and `GET /webhook` (from `app.all`, reported as `GET`). `GET /legacy/orders/:id` must NOT appear.

See test-plan-apisec-framework-discovery.html.
