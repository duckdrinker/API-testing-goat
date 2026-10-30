// #2214 - owner bound through a local variable (Express + Sequelize).
// NOT REPORTED cases must stay silent; REPORTED cases must fire
// javascript.user_controlled_primary_key.
const express = require('express');
const { Sequelize, DataTypes } = require('sequelize');

const sequelize = new Sequelize('sqlite::memory:');
const Order = sequelize.define('Order', {
  owner: DataTypes.STRING,
  status: DataTypes.STRING,
});

const router = express.Router();

// CTRL-1 REPORTED: unbounded lookup, baseline positive.
router.get('/ctrl1/:id', async (req, res) => {
  const order = await Order.findByPk(req.params.id);
  res.json(order);
});

// CTRL-2 NOT REPORTED: caller written inline in the bound.
router.get('/ctrl2/:id', async (req, res) => {
  const order = await Order.findOne({ where: { id: req.params.id, owner: req.user.name } });
  res.json(order);
});

// A1 NOT REPORTED: bound in the lookup, caller held in a local.
router.get('/a1/:id', async (req, res) => {
  const owner = req.user.name;
  const order = await Order.findOne({ where: { id: req.params.id, owner } });
  res.json(order);
});

// A2 REPORTED: twin of A1, the local holds a request value.
router.get('/a2/:id', async (req, res) => {
  const owner = req.query.owner;
  const order = await Order.findOne({ where: { id: req.params.id, owner } });
  res.json(order);
});

// B1 NOT REPORTED: comparison after the lookup, caller held in a local.
router.get('/b1/:id', async (req, res) => {
  const owner = req.user.name;
  const order = await Order.findByPk(req.params.id);
  if (!order || order.owner !== owner) return res.status(403).end();
  res.json(order);
});

// B2 REPORTED: twin of B1, the local holds a request value.
router.get('/b2/:id', async (req, res) => {
  const owner = req.query.owner;
  const order = await Order.findByPk(req.params.id);
  if (!order || order.owner !== owner) return res.status(403).end();
  res.json(order);
});

// C1 REPORTED: local first takes the caller, then is rebound to a request value.
router.get('/c1/:id', async (req, res) => {
  let owner = req.user.name;
  owner = req.query.owner;
  const order = await Order.findOne({ where: { id: req.params.id, owner } });
  res.json(order);
});

// C2 REPORTED: rebound local, comparison after the lookup.
router.get('/c2/:id', async (req, res) => {
  let owner = req.user.name;
  owner = req.query.owner;
  const order = await Order.findByPk(req.params.id);
  if (!order || order.owner !== owner) return res.status(403).end();
  res.json(order);
});

// D1 REPORTED: loop variable, not assigned once from the caller.
router.get('/d1/:id', async (req, res) => {
  let found = null;
  for (const owner of req.query.owners) {
    found = await Order.findOne({ where: { id: req.params.id, owner } });
  }
  res.json(found);
});

module.exports = router;
