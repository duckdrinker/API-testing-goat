const express = require('express');
const router = express.Router();
function readToken(req) { return req.headers['x-token']; }

router.post('/auto-login', (req, res) => {
  const token = readToken(req);
  req.session.user = { id: token };
  res.json({ ok: true });
});

module.exports = router;
