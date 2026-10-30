const express = require('express');
const jwt = require('jsonwebtoken');
const router = express.Router();
const SECRET = process.env.JWT_SECRET;
router.post('/revoke', (req, res) => {
  req.session.user = null;
  req.session.destroy(() => res.json({ ok: true }));
});

module.exports = router;
