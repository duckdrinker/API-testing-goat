const express = require('express');
const jwt = require('jsonwebtoken');
const router = express.Router();
const SECRET = process.env.JWT_SECRET;
function verifyToken(token) { return jwt.verify(token, SECRET); }

router.post('/auto-login', (req, res) => {
  req.session.user = { id: verifyToken(req.headers['x-token']).sub };
  res.json({ ok: true });
});

module.exports = router;
