'use strict';

function auth(req, res, next) {
  if (!req.headers.authorization) {
    return res.status(401).end();
  }
  next();
}

function createOrder(req, res) {
  const order = { id: 1, items: req.body.items };
  res.status(201).json(order);
}

module.exports = { auth, createOrder };
