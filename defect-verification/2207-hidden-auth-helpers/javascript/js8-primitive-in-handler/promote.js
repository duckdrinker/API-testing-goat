const express = require('express');
const jwt = require('jsonwebtoken');
const router = express.Router();
const SECRET = process.env.JWT_SECRET;
router.post('/auto-login', (req, res) => {
  const claims = jwt.verify(req.headers['x-token'], SECRET);
  req.session.user = { id: claims.sub };
  res.json({ ok: true });
});

module.exports = router;
