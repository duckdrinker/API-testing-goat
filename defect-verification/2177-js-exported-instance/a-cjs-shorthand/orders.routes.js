const express = require('express');
const { orderStore } = require('./order-store');
const router = express.Router();
router.post('/orders', (req, res) => { orderStore.save(req.body.number, req.body.order); res.sendStatus(201); });
module.exports = router;
