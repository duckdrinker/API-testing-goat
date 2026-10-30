const express = require('express');
const jwt = require('jsonwebtoken');
const router = express.Router();
const SECRET = process.env.JWT_SECRET;
function readToken(req) { return req.headers['x-token']; }

router.post('/auto-login', (req, res) => {
  const token = readToken(req);
  const claims = jwt.verify(token, SECRET);
  req.session.user = { id: claims.sub };
  res.json({ ok: true });
});

module.exports = router;
