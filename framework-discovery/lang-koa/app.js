// lang-koa fixture -- API Discovery framework detection (KoaFrameworkDetector).
// This file is a static-analysis fixture, not meant to be run. Some mount patterns
// below are deliberately written to exercise specific, documented detector
// behaviours -- both the supported Express-style mount and two known limitations
// (see README.md).

const Koa = require('koa');
const Router = require('@koa/router');
const passport = require('koa-passport');

const app = new Koa();

// --- Router 1: mounted with the Express-style string-prefixed mount. -------------
// KoaFrameworkDetector DOES recognize `app.use('/prefix', router)`, the same pattern
// ExpressFrameworkDetector recognizes for Express routers.
const router = new Router();

router.get('/api/items/:id', (ctx) => {
  ctx.body = { id: ctx.params.id };
});

router.post('/api/items', (ctx) => {
  ctx.body = { created: true };
});

// koa-passport middleware in the route chain -> recognized as an auth-protected route.
router.get('/api/private', passport.authenticate('jwt'), (ctx) => {
  ctx.body = { secret: true };
});

app.use('/mounted', router);

// --- Router 2: created with a Router-level `prefix` option. ----------------------
// KNOWN LIMITATION: the `prefix` option is not applied by KoaFrameworkDetector.
// Expected discovery: GET /v2mount/widgets/:id (mount path only) -- NOT
// GET /v2mount/v2/widgets/:id. See README.md.
const router2 = new Router({ prefix: '/v2' });

router2.get('/widgets/:id', (ctx) => {
  ctx.body = { id: ctx.params.id };
});

app.use('/v2mount', router2);

// --- Router 3: mounted with the real, idiomatic Koa pattern. ---------------------
// KNOWN LIMITATION: `app.use(router.routes())` -- the actual idiomatic way to mount
// a @koa/router instance in Koa -- is not recognized at all by KoaFrameworkDetector,
// which only understands the Express-style string-prefixed mount used for router/router2
// above. Expected discovery: this route is entirely ABSENT from the output, under any
// path. See README.md.
const router3 = new Router();

router3.get('/hidden/:id', (ctx) => {
  ctx.body = { id: ctx.params.id };
});

app.use(router3.routes());
app.use(router3.allowedMethods());

app.listen(3000, () => console.log('lang-koa fixture listening on port 3000'));
