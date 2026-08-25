// lang-hono fixture -- API Discovery framework detection (HonoFrameworkDetector).
// Static-analysis fixture, not meant to be run.

import { Hono } from 'hono';
import type { Context, Next } from 'hono';

function requireAuth(c: Context, next: Next) {
  const auth = c.req.header('authorization');
  if (!auth) {
    return c.text('Unauthorized', 401);
  }
  return next();
}

// Main app -- the first `new Hono()` in the module.
const app = new Hono();

// --- Sub-router 1: mounted via app.route('/prefix', subApp). ---------------------
// HonoFrameworkDetector recognizes this mount and applies the '/api' prefix to
// every route declared on `api`.
const api = new Hono();

api.get('/items/:id', (c) => {
  const id = c.req.param('id');
  return c.json({ id });
});

app.route('/api', api);

// --- Sub-router 2: auth middleware declared at the sub-router level. --------------
// `admin.use('*', requireAuth)` is recognized as router-level security, inherited
// by every route declared on `admin` below.
const admin = new Hono();

admin.use('*', requireAuth);

admin.get('/users', (c) => {
  return c.json({ users: [] });
});

app.route('/admin', admin);

// Catch-all verb on the main app.
app.all('/webhook', (c) => {
  return c.json({ received: true });
});

// --- Sub-router 3: mounted via app.basePath('/legacy') instead of app.route(). ----
// KNOWN LIMITATION: `app.basePath('/prefix')` is not resolved by HonoFrameworkDetector
// (unlike `app.route('/prefix', subApp)`, which is). See README.md.
const legacy = new Hono();

legacy.get('/orders/:id', (c) => {
  const id = c.req.param('id');
  return c.json({ id });
});

app.basePath('/legacy').route('/', legacy);

export default app;
