/**
 * Koa side of the mixed-signals fixture. See requirements.txt for the
 * sibling Flask (Python) manifest and app.py for the sibling Flask
 * endpoint. Both frameworks' manifests declare a manifest-hit-level
 * dependency (confidence 0.95 each).
 */
const Koa = require('koa');
const Router = require('@koa/router');

const app = new Koa();
const router = new Router();

router.get('/api/ping-koa', (ctx) => {
  ctx.body = { status: 'ok' };
});

app.use(router.routes());

app.listen(3000, () => console.log('autodiscovery-mixed-signals (koa) listening on port 3000'));
