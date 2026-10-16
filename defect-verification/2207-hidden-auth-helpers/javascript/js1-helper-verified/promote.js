const express = require('express');
const jwt = require('jsonwebtoken');
const router = express.Router();
const SECRET = process.env.JWT_SECRET;
function verifyToken(token) { return jwt.verify(token, SECRET); }

router.post('/auto-login', (req, res) => {
  const claims = verifyToken(req.headers['x-token']);
  req.session.user = { id: claims.sub };
  res.json({ ok: true });
});

module.exports = router;
