const express = require('express');
const jwt = require('jsonwebtoken');
const router = express.Router();
const SECRET = process.env.JWT_SECRET;
function parse(token) { return jwt.verify(token, SECRET); }
function subjectFrom(token) { return parse(token).sub; }

router.post('/auto-login', (req, res) => {
  const sub = subjectFrom(req.headers['x-token']);
  req.session.user = { id: sub };
  res.json({ ok: true });
});

module.exports = router;
