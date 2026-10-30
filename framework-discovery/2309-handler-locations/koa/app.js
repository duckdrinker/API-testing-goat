'use strict';
const Koa = require('koa');
const Router = require('@koa/router');
const { getUser } = require('./handlers/users');

const app = new Koa();
const router = new Router();

router.get('/users/:id', getUser);

router.post('/inline', async (ctx) => {
  const body = ctx.request.body;
  ctx.status = 201;
  ctx.body = {
    created: body,
  };
});

app.use(router.routes());
app.listen(3000);
