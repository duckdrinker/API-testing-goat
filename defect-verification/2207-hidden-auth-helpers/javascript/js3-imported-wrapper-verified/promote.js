const express = require('express');
const { decode } = require('./jwt-utils');
const router = express.Router();

router.post('/auto-login', (req, res) => {
  const claims = decode(req.headers['x-token']);
  req.session.user = { id: claims.sub };
  res.json({ ok: true });
});
module.exports = router;
