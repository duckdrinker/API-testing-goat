const express = require('express');
const { OrderStore } = require('./order-store');
const router = express.Router();
router.post('/orders', (req, res) => { new OrderStore().save(req.body.number, req.body.order); res.sendStatus(201); });
module.exports = router;
