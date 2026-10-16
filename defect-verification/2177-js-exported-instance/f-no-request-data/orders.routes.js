const express = require('express');
const { orderStore } = require('./order-store');
const router = express.Router();
router.post('/orders', (req, res) => { orderStore.save('fixed', 'fixed'); res.sendStatus(201); });
module.exports = router;
