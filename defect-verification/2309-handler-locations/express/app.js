'use strict';
const express = require('express');
const { login } = require('./handlers/login');
const { makeHandler } = require('./handlers/factory');
const { auth, createOrder } = require('./handlers/auth');
const itemsRouter = require('./handlers/router');
const ping = require('./handlers/defaultHandler');
const Svc = require('./handlers/Svc');

const app = express();
app.use(express.json());

app.post('/login', login);
app.post('/factory', makeHandler());
app.use('/api', itemsRouter);

app.get('/inline', (req, res) => {
  const a = 1;
  const b = 2;
  res.json({
    sum: a + b,
  });
});

app.get('/ping', ping);
app.get('/svc', new Svc({}).handle);
app.post('/orders', auth, createOrder);

function localHandler(req, res) {
  res.send('local');
}
app.get('/local', localHandler);

app.listen(3000);
