import { Hono } from 'hono';
import { listUsers } from './handlers/users.js';

const app = new Hono();

app.get('/users', listUsers);

app.post('/inline', async (c) => {
  const body = await c.req.json();
  return c.json({
    created: body,
  });
});

export default app;
