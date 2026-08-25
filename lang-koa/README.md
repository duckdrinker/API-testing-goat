# Koa — API Discovery framework coverage

**Xygeni component:** API Discovery framework detector `KoaFrameworkDetector`

## What this fixture demonstrates
- `router.get('/api/items/:id', ...)` (`GET /mounted/api/items/:id`) — path param `:id` extracted from `ctx.params.id` → framework discovery.
- `router.post('/api/items', ...)` (`POST /mounted/api/items`) — basic route on the same router → framework discovery.
- `router.get('/api/private', passport.authenticate('jwt'), ...)` (`GET /mounted/api/private`) — `koa-passport` middleware in the route chain → recognized as an auth-protected route.
- `app.use('/mounted', router)` — the Express-style string-prefixed mount, which `KoaFrameworkDetector` recognizes and correctly applies to every route on `router` above.
- `router2.get('/widgets/:id', ...)` mounted via `app.use('/v2mount', router2)`, where `router2 = new Router({ prefix: '/v2' })` — exercises the first known limitation below.
- `router3.get('/hidden/:id', ...)` mounted via `app.use(router3.routes())` — exercises the second known limitation below.

## Known limitation demonstrated
- **The `prefix` option of `new Router({ prefix })` is not applied.** `router2` is created with `{ prefix: '/v2' }`, which `@koa/router` would normally prepend to every route registered on it at runtime. `KoaFrameworkDetector` ignores this constructor option entirely, so `router2.get('/widgets/:id', ...)` is expected to be discovered as `GET /v2mount/widgets/:id` (the `app.use('/v2mount', router2)` mount path alone) — **not** `GET /v2mount/v2/widgets/:id`. This is documented, expected behaviour of the detector, not a bug to fix here.
- **The idiomatic Koa mount `app.use(router.routes())` is not detected at all.** `KoaFrameworkDetector`'s mount-prefix detection (inherited from the shared `JsProgrammaticRoutingDetector` base) only understands the Express-style string-prefixed mount `app.use('/prefix', router)` — the pattern used for `router`/`router2` above. `router3` is mounted the real, idiomatic Koa way, `app.use(router3.routes())`, with no string prefix argument at all. `router3.get('/hidden/:id', ...)` is expected to be **entirely absent** from the discovery output, under any path. This fixture deliberately keeps this pattern rather than "fixing" it to the Express style, precisely to exercise the gap.

## Expected scan result
```
xygeni apisecurity --dir . --discovery-only -f json
```
Discovered endpoints should include `GET /mounted/api/items/:id`, `POST /mounted/api/items`, `GET /mounted/api/private` (auth-protected), and `GET /v2mount/widgets/:id`. No path derived from `router3` (e.g. `/hidden/:id`, prefixed or not) should appear.

See test-plan-apisec-framework-discovery.html.
