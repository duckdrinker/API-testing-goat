const express = require('express');
const jwt = require('jsonwebtoken');
const router = express.Router();
const SECRET = process.env.JWT_SECRET;
router.post('/auto-login', (req, res) => {
  req.session.user = { id: req.body.userId };
  res.json({ ok: true });
});

module.exports = router;
