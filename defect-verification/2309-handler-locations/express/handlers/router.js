'use strict';
const express = require('express');
const router = express.Router();

function listItems(req, res) {
  res.json([]);
}

router.get('/items', listItems);

module.exports = router;
