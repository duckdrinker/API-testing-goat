# Fastify — API Discovery framework coverage

**Xygeni component:** API Discovery framework detector `FastifyFrameworkDetector`

## What this fixture demonstrates
- `fastify.get('/api/items/:id', handler)` (`GET /api/items/:id`) — shorthand route form, path param `:id` → framework discovery.
- `fastify.post('/api/items', handler)` (`POST /api/items`) — shorthand route form → framework discovery.
- `fastify.route({ method: 'GET', url: '/api/private', preHandler: requireAuth, handler })` (`GET /api/private`) — full route form, `preHandler` as a single function → recognized as an auth-protected route.
- `fastify.route({ method: 'POST', url: '/api/admin', preHandler: [requireAuth, requireRole('admin')], handler })` (`POST /api/admin`) — full route form, `preHandler` as an array — both entries inspected as auth middleware, same as the single-value form → recognized as an auth-protected route.
- `fastify.all('/api/webhook', handler)` (`GET /api/webhook`, `all` reported as `GET`) — catch-all verb → framework discovery.
- `fastify.register(v2Plugin, { prefix: '/v2' })` (`v2Plugin` defined in `plugin.js`) — exercises the known limitation below.

## Known limitation demonstrated
- **`fastify.register(plugin, { prefix })` is not resolved across files.** Determining which routes belong to a registered plugin requires tracing the plugin function from its definition back to the `register()` call site — cross-file data flow that `FastifyFrameworkDetector` does not implement. `v2Plugin` is deliberately kept in a separate file, `plugin.js` (rather than inlined into `app.js`), specifically to exercise this gap: `GET /status` and `GET /info` (declared inside `v2Plugin`) are expected to be **entirely absent** from the discovery output — not as `/status`/`/info`, and not as `/v2/status`/`/v2/info`. This is documented, expected behaviour of the detector, not a bug to fix here.

## Expected scan result
```
xygeni apisecurity --dir . --discovery-only -f json
```
Discovered endpoints should include `GET /api/items/:id`, `POST /api/items`, `GET /api/private` (auth-protected), `POST /api/admin` (auth-protected), and `GET /api/webhook` (from `fastify.all`, reported as `GET`). `GET /status`, `GET /info`, `GET /v2/status` and `GET /v2/info` must NOT appear.

See test-plan-apisec-framework-discovery.html.
